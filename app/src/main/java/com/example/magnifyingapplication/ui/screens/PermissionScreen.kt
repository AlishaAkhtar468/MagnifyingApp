package com.example.magnifyingapplication.ui.screens

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magnifyingapplication.viewmodel.PermissionsViewModel
import com.example.magnifyingapplication.R
import com.example.magnifyingapplication.data.PreferencesManager
import com.google.accompanist.permissions.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    viewModel: PermissionsViewModel = viewModel(),
    onPermissionGranted: () -> Unit
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val isGranted by viewModel.cameraPermissionGranted.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var permissionRequested by remember { mutableStateOf(false) }
    var showPermissionDeniedDialog by remember { mutableStateOf(false) }

    // 🔁 Handle permission result
    LaunchedEffect(cameraPermissionState.status, permissionRequested) {
        if (permissionRequested) {
            permissionRequested = false

            when (cameraPermissionState.status) {
                is PermissionStatus.Granted -> {
                    viewModel.setCameraPermission(true)
                    coroutineScope.launch {
                        preferencesManager.setCameraPermissionGranted(true)
                    }
                    onPermissionGranted()
                }

                is PermissionStatus.Denied -> {
                    viewModel.setCameraPermission(false)
                    showPermissionDeniedDialog = true
                }
            }
        }
    }

    // ❗ Show settings dialog on denial
    if (showPermissionDeniedDialog) {
        AlertDialog(
            onDismissRequest = { showPermissionDeniedDialog = false },
            title = { Text("Permission Required") },
            text = { Text("Camera access is required to capture photos. Please allow it in settings.") },
            confirmButton = {
                TextButton(onClick = {
                    showPermissionDeniedDialog = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text("Open Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPermissionDeniedDialog = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    // 📱 UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Grant Permissions",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF001F5B)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "This application needs access to the\nfollowing permissions",
            fontSize = 14.sp,
            color = Color.Gray,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.ccamera),
            contentDescription = "Camera Icon",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFF4F4F4)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Camera", fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("To Capture Photos", fontSize = 12.sp, color = Color.DarkGray)
                }

                Switch(
                    checked = isGranted,
                    onCheckedChange = {
                        permissionRequested = true
                        cameraPermissionState.launchPermissionRequest()
                    }
                )
            }
        }
    }
}
