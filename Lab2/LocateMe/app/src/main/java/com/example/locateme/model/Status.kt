package com.example.locateme.model

import android.net.wifi.WifiInfo
import androidx.compose.Model
import androidx.compose.frames.ModelList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

sealed class Screen {
    object Home: Screen()
    object StoredAP: Screen()
}

@Model
object LocateMeStatus {
    var currentConnection : WifiInfo? = null
    var accessPoints = ModelList<AccessPoint>()
    var currentScreen: Screen = Screen.Home
    var storedPositions = ModelList<Position>()
}

fun navigateTo(destination: Screen) {
    LocateMeStatus.currentScreen = destination
}
