package com.example.magnifyingapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toIntSize
import androidx.navigation.compose.rememberNavController
import com.example.magnifyingapplication.ui.theme.MagnifyingApplicationTheme

class EnhancerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MagnifyingApplicationTheme {
                val navController = rememberNavController()
                ResultScreen(navController = navController)
            }
        }
    }
}

@Composable
fun ResultScreen(navController: androidx.navigation.NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Result")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* If needed: (context as Activity).finish() */ }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                actions = {
                    TextButton(onClick = { /* Done action */ }) {
                        Text("Done")
                    }
                },
                backgroundColor = Color.White,
                elevation = 0.dp
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = { /* Share action */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share", modifier = Modifier.size(32.dp))
                }
                IconButton(onClick = { /* Download action */ }) {
                    Icon(Icons.Default.Download, contentDescription = "Download", modifier = Modifier.size(32.dp))
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            CenteredImageCompareSlider()
        }
    }
}

@Composable
fun CenteredImageCompareSlider() {
    val clearImage = ImageBitmap.imageResource(id = R.drawable.clear_images)
    val blurImage = ImageBitmap.imageResource(id = R.drawable.imagesblur)

    var sliderX by remember { mutableStateOf(0.5f) }

    val imageRatio = clearImage.width.toFloat() / clearImage.height.toFloat()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(imageRatio)
            .clip(RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    sliderX = (sliderX + dragAmount / size.width).coerceIn(0f, 1f)
                }
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawImage(
                image = blurImage,
                dstSize = size.toIntSize()
            )

            clipRect(
                left = 0f,
                top = 0f,
                right = size.width * sliderX,
                bottom = size.height,
                clipOp = ClipOp.Intersect
            ) {
                drawImage(
                    image = clearImage,
                    dstSize = size.toIntSize()
                )
            }

            val sliderPosX = size.width * sliderX

            drawLine(
                color = Color.White,
                start = Offset(sliderPosX, 0f),
                end = Offset(sliderPosX, size.height),
                strokeWidth = 4.dp.toPx()
            )

            drawCircle(
                color = Color.White,
                radius = 20.dp.toPx(),
                center = Offset(sliderPosX, size.height / 2)
            )
        }

        Text(
            "Before",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )

        Text(
            "After",
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .background(Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
