package com.example.magnifyingapplication.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "app_preferences")

class PreferencesManager(private val context: Context) {

    companion object {
        private val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        private val CAMERA_PERMISSION_GRANTED = booleanPreferencesKey("camera_permission_granted")
    }

    val onboardingCompleted: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[ONBOARDING_COMPLETED] ?: false }

    val cameraPermissionGranted: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[CAMERA_PERMISSION_GRANTED] ?: false }

    suspend fun setOnboardingCompleted() {
        context.dataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETED] = true
        }
    }

      suspend fun setCameraPermissionGranted(granted: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[CAMERA_PERMISSION_GRANTED] = granted
        }
    }

}
