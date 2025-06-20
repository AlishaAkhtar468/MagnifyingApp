// PermissionsViewModel.kt
package com.example.magnifyingapplication.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PermissionsViewModel : ViewModel() {
    private val _cameraPermissionGranted = MutableStateFlow(false)
    val cameraPermissionGranted: StateFlow<Boolean> = _cameraPermissionGranted

    fun setCameraPermission(granted: Boolean) {
        _cameraPermissionGranted.value = granted
    }
}
