package com.example.magnifyingapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.magnifyingapplication.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import com.example.magnifyingapplication.model.OnboardingData

class OnboardingViewModel : ViewModel() {

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage

    val onboardingItems = listOf(
        OnboardingData(
            R.drawable.zoom_screen,
            "Adjust Zoom",
            "Drag up & Down to Adjust\nMagnifying Zoom Level",
            R.drawable.lightprogress1
        ),
        OnboardingData(
            R.drawable.slider2,
            "Adjust Brightness",
            "Drag Right & Left To\nAdjust Screen Brightness Level",
            R.drawable.lightprogress2
        ),
        OnboardingData(
            R.drawable.capture,
            "Capture Photo",
            "Tap And Hold To Capture Image",
            R.drawable.lightprogress3
        ),
        OnboardingData(
            R.drawable.slider4,
            "AI Enhancer",
            "Swipe Left or Right To\nImprove Image Quality Instantly",
            R.drawable.lightprogress4
        )
    )


    fun nextPage() {
        if (_currentPage.value < onboardingItems.size - 1) {
            _currentPage.value += 1
        }
    }

    fun previousPage() {
        if (_currentPage.value > 0) {
            _currentPage.value -= 1
        }
    }

    fun skip() {
        _currentPage.value = onboardingItems.size - 1
    }
}
