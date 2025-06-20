package com.example.magnifyingapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magnifyingapplication.ui.screens.*
import com.example.magnifyingapplication.viewmodel.SplashViewModel
import com.example.magnifyingapplication.ui.theme.MagnifyingApplicationTheme
import com.example.magnifyingapplication.ui.viewmodel.OnboardingViewModel
import com.example.magnifyingapplication.viewmodel.PermissionsViewModel
import com.example.magnifyingapplication.data.PreferencesManager
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val splashViewModel: SplashViewModel = viewModel()
            val onboardingViewModel: OnboardingViewModel = viewModel()
            val permissionsViewModel: PermissionsViewModel = viewModel()
            val isDark by splashViewModel.isDarkTheme.collectAsState()

            val context = LocalContext.current
            val preferencesManager = remember { PreferencesManager(context) }
            val onboardingCompleted by preferencesManager.onboardingCompleted.collectAsState(initial = false)
            var currentScreen by remember { mutableStateOf("splash") }
            val coroutineScope = rememberCoroutineScope()

            MagnifyingApplicationTheme(darkTheme = isDark) {
                when (currentScreen) {
                    "splash" -> SplashScreen(
                        onGetStarted = {
                            currentScreen = if (onboardingCompleted) "camera" else "onboarding"
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
                        viewModel = permissionsViewModel,
                        onPermissionGranted = {
                            currentScreen = "camera"
                        }
                    )

                    "camera" -> CameraScreen()
                }
            }
        }
    }
}
