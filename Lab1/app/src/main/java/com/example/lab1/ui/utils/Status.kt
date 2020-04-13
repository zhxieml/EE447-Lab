package com.example.lab1.ui.utils

import androidx.compose.Model

sealed class Screen {
    object Home: Screen()
    object DialPad: Screen()
}

@Model
object EasyDialerStatus {
    var currentScreen: Screen = Screen.Home
}

fun navigateTo(destination: Screen) {
    EasyDialerStatus.currentScreen = destination
}