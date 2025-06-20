package com.example.magnifyingapplication.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CameraViewModel : ViewModel() {
    private val _zoomLevel = MutableStateFlow(1f)
    val zoomLevel: StateFlow<Float> = _zoomLevel

    fun updateZoomLevel(value: Float) {
        _zoomLevel.value = value.coerceIn(1f, 10f) // clamp zoom between 1x and 10x
    }
}
