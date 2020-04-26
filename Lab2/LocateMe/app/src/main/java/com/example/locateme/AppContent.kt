package com.example.locateme

import androidx.compose.Composable
import androidx.ui.animation.Crossfade
import androidx.ui.material.MaterialTheme
import androidx.ui.material.surface.Surface
import com.example.locateme.model.LocateMeStatus
import com.example.locateme.model.Screen
import com.example.locateme.ui.HomeScreen
import com.example.locateme.ui.StoredPositionScreen

@Composable
fun AppContent() {
    Crossfade(current = LocateMeStatus.currentScreen) { screen ->
        Surface(color = MaterialTheme.colors().background) {
            when (screen) {
                is Screen.Home -> HomeScreen()
                is Screen.StoredAP -> StoredPositionScreen()
            }
        }
    }
}