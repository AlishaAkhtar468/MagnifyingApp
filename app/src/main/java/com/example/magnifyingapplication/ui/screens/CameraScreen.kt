package com.example.magnifyingapplication.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val lensFacing = rememberSaveable { mutableStateOf(CameraSelector.LENS_FACING_BACK) }
    var zoomLevel by rememberSaveable { mutableStateOf(0.5f) }
    var frozenZoom by rememberSaveable { mutableStateOf(1f) }
    var brightness by rememberSaveable { mutableStateOf(0.5f) }
    var isFlashOn by rememberSaveable { mutableStateOf(false) }
    var isFrozen by rememberSaveable { mutableStateOf(false) }

    var camera: Camera? by remember { mutableStateOf(null) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var previewView: PreviewView? by remember { mutableStateOf(null) }
    var frozenBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val overlayAlpha = remember { derivedStateOf { 1f - brightness } }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) Log.e("Camera", "Camera permission denied")
    }

    LaunchedEffect(true) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    fun bindCameraUseCases() {
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView?.surfaceProvider)
        }

        val capture = ImageCapture.Builder()
            .setFlashMode(ImageCapture.FLASH_MODE_OFF)
            .build()

        val selector = CameraSelector.Builder()
            .requireLensFacing(lensFacing.value)
            .build()

        cameraProvider.unbindAll()
        val cam = cameraProvider.bindToLifecycle(lifecycleOwner, selector, preview, capture)
        cam.cameraControl.setLinearZoom(zoomLevel)

        camera = cam
        imageCapture = capture
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!isFrozen) {
            AndroidView(factory = { ctx ->
                val view = PreviewView(ctx)
                previewView = view
                cameraProviderFuture.addListener({
                    bindCameraUseCases()
                }, ContextCompat.getMainExecutor(ctx))
                view
            }, modifier = Modifier.fillMaxSize())
        }

        frozenBitmap?.let { bitmap ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = frozenZoom
                        scaleY = frozenZoom
                        transformOrigin = TransformOrigin.Center
                    }
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Frozen Frame",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = overlayAlpha.value))
        )

        // 🔄 Switch camera button (moved down)
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 12.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF333333).copy(alpha = 0.7f)
        ) {
            IconButton(onClick = {
                lensFacing.value =
                    if (lensFacing.value == CameraSelector.LENS_FACING_BACK)
                        CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK
                frozenBitmap = null
                isFrozen = false
                bindCameraUseCases()
            }) {
                Icon(Icons.Default.Cameraswitch, contentDescription = "Switch", tint = Color.White)
            }
        }

        // ⬅️ Brightness slider on the left side
        BrightnessSlider(
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 12.dp),
            brightness = brightness,
            onChange = { brightness = it }
        )

        // 🔍 Zoom controls moved up
        ZoomControls(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp),
            zoomLevel = zoomLevel,
            frozenZoom = frozenZoom,
            isFrozen = isFrozen,
            onZoomChange = {
                if (isFrozen) frozenZoom = it
                else {
                    zoomLevel = it
                    camera?.cameraControl?.setLinearZoom(it)
                }
            }
        )

        // 🎮 Bottom controls bar (more spacing)
        BottomControls(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp),
            isFrozen = isFrozen,
            isFlashOn = isFlashOn,
            onFreezeToggle = {
                if (!isFrozen && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    previewView?.bitmap?.let {
                        frozenBitmap = it
                        frozenZoom = 1f
                        isFrozen = true
                        camera?.cameraControl?.enableTorch(false)
                        isFlashOn = false
                        cameraProviderFuture.get().unbindAll()
                    }
                } else {
                    frozenBitmap = null
                    isFrozen = false
                    bindCameraUseCases()
                }
            },
            onCapture = {
                frozenBitmap = null
                isFrozen = false
                imageCapture?.let { capture ->
                    val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    val file = File(
                        outputDir,
                        "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
                    )
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

                    capture.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                Log.d("CameraScreen", "Photo saved: ${file.absolutePath}")
                                Toast.makeText(context, "Picture saved!", Toast.LENGTH_SHORT).show()
                            }

                            override fun onError(exception: ImageCaptureException) {
                                Log.e("CameraScreen", "Capture failed: ${exception.message}", exception)
                                Toast.makeText(context, "Capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
            },
            onFlashToggle = {
                isFlashOn = !isFlashOn
                camera?.cameraControl?.enableTorch(isFlashOn)
            }
        )
    }
}

@Composable
fun ZoomControls(
    modifier: Modifier = Modifier,
    zoomLevel: Float,
    frozenZoom: Float,
    isFrozen: Boolean,
    onZoomChange: (Float) -> Unit
) {
    val zoom = if (isFrozen) frozenZoom else zoomLevel
    val step = if (isFrozen) 0.1f else 0.05f
    val minZoom = if (isFrozen) 1f else 0f
    val maxZoom = if (isFrozen) 3f else 1f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            val newZoom = (zoom - step).coerceIn(minZoom, maxZoom)
            onZoomChange(newZoom)
        }) {
            Icon(Icons.Default.ZoomOut, contentDescription = "Zoom Out", tint = Color.White)
        }

        Slider(
            value = zoom,
            onValueChange = { onZoomChange(it) },
            valueRange = minZoom..maxZoom,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color.Magenta,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.Gray
            )
        )

        IconButton(onClick = {
            val newZoom = (zoom + step).coerceIn(minZoom, maxZoom)
            onZoomChange(newZoom)
        }) {
            Icon(Icons.Default.ZoomIn, contentDescription = "Zoom In", tint = Color.White)
        }
    }
}

@Composable
fun BrightnessSlider(
    modifier: Modifier = Modifier,
    brightness: Float,
    onChange: (Float) -> Unit
) {
    Column(
        modifier = modifier
            .width(72.dp)
            .height(300.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center // 👈 Tight vertical spacing
    ) {
        IconButton(
            onClick = { onChange(1f) },
            modifier = Modifier.size(36.dp) // Smaller spacing around icon
        ) {
            Icon(
                Icons.Default.WbSunny,
                contentDescription = "Max Brightness",
                tint = Color.Yellow,
                modifier = Modifier.size(28.dp) // Icon size
            )
        }

        Spacer(modifier = Modifier.height(8.dp)) // Small space between icon and slider

        Slider(
            value = brightness,
            onValueChange = onChange,
            valueRange = 0f..1f,
            modifier = Modifier
                .fillMaxHeight(0.6f)
                .rotate(270f),
            colors = SliderDefaults.colors(
                thumbColor = Color.Yellow,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp)) // Small space between slider and icon

        IconButton(
            onClick = { onChange(0f) },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                Icons.Default.WbSunny,
                contentDescription = "Min Brightness",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


@Composable
fun BottomControls(
    modifier: Modifier = Modifier,
    isFrozen: Boolean,
    isFlashOn: Boolean,
    onFreezeToggle: () -> Unit,
    onCapture: () -> Unit,
    onFlashToggle: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleIconButton(icon = Icons.Default.Settings, contentDesc = "Settings")

        CircleIconButton(
            icon = if (isFrozen) Icons.Default.Cancel else Icons.Default.AcUnit,
            contentDesc = "Freeze",
            tint = if (isFrozen) Color.Red else Color.White,
            onClick = onFreezeToggle
        )

        Surface(
            onClick = onCapture,
            shape = RoundedCornerShape(50),
            color = Color.Gray.copy(alpha = 0.4f),
            modifier = Modifier.size(72.dp),
            tonalElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.CameraAlt,
                    contentDescription = "Capture",
                    tint = Color(0xFF0E2C66),
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        CircleIconButton(icon = Icons.Default.AutoAwesome, contentDesc = "AI")

        CircleIconButton(
            icon = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
            contentDesc = "Flash",
            tint = Color.Yellow,
            onClick = onFlashToggle
        )
    }
}

@Composable
fun CircleIconButton(
    icon: ImageVector,
    contentDesc: String,
    tint: Color = Color.White,
    onClick: () -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.Gray.copy(alpha = 0.3f),
        modifier = Modifier.size(48.dp)
    ) {
        IconButton(onClick = onClick) {
            Icon(icon, contentDescription = contentDesc, tint = tint)
        }
    }
}