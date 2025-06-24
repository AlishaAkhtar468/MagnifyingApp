package com.example.magnifyingapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magnifyingapplication.data.PreferencesManager
import com.example.magnifyingapplication.ui.screens.*
import com.example.magnifyingapplication.ui.theme.MagnifyingApplicationTheme
import com.example.magnifyingapplication.ui.viewmodel.OnboardingViewModel
import com.example.magnifyingapplication.ui.viewmodel.SplashViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val splashViewModel: SplashViewModel = viewModel()
            val onboardingViewModel: OnboardingViewModel = viewModel()
            val isDark by splashViewModel.isDarkTheme.collectAsState()

            val context = LocalContext.current
            val preferencesManager = remember { PreferencesManager(context) }

            val onboardingCompleted by preferencesManager.onboardingCompleted.collectAsState(initial = false)
            val cameraPermissionGranted by preferencesManager.cameraPermissionGranted.collectAsState(initial = false)

            var currentScreen by remember { mutableStateOf("splash") }
            val coroutineScope = rememberCoroutineScope()

            MagnifyingApplicationTheme(darkTheme = isDark) {
                when (currentScreen) {
                    "splash" -> SplashScreen(
                        onGetStarted = {
                            currentScreen = when {
                                !onboardingCompleted -> "onboarding"
                                !cameraPermissionGranted -> "permission"
                                else -> "camera"
                            }
                        },
                        viewModel = splashViewModel
                    )

                    "onboarding" -> OnboardingScreen(
                        viewModel = onboardingViewModel,
                        onFinish = {
                            coroutineScope.launch {
                                preferencesManager.setOnboardingCompleted()
                            }
                            currentScreen = "permission"
                        },
                        onSkip = {
                            coroutineScope.launch {
                                preferencesManager.setOnboardingCompleted()
                            }
                            currentScreen = "permission"
                        }
                    )

                    "permission" -> PermissionScreen(
                        onPermissionGranted = {
                            coroutineScope.launch {
                                preferencesManager.setCameraPermissionGranted(true)
                            }
                            currentScreen = "camera"
                        }
                    )

                    "camera" -> CameraScreen()
                }
            }
        }
    }
}
