package com.example.magnifyingapplication.ui.screens

import android.Manifest
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magnifyingapplication.viewmodel.PermissionsViewModel
import com.example.magnifyingapplication.R
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(
    viewModel: PermissionsViewModel = viewModel(),
    onPermissionGranted: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val isGranted by viewModel.cameraPermissionGranted.collectAsState()

    LaunchedEffect(cameraPermissionState.status) {
        val granted = cameraPermissionState.status.isGranted
        viewModel.setCameraPermission(granted)
        if (granted) {
            onPermissionGranted()
        }
    }

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
            lineHeight = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
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
                        if (!cameraPermissionState.status.isGranted) {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    }
                )
            }
        }
    }
}
