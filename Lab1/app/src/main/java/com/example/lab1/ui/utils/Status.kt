package com.example.lab1.ui.utils

import androidx.compose.Model
import androidx.compose.frames.ModelList
import com.example.lab1.data.initialContacts
import com.example.lab1.entity.Contact

sealed class Screen {
    object Home: Screen()
    object DialPad: Screen()
}

@Model
object EasyDialerStatus {
    var currentScreen: Screen = Screen.Home
    var contacts = ModelList<Contact>()
    var isCalling = false
    
    init {
        contacts.addAll(initialContacts)
    }
}

fun navigateTo(destination: Screen) {
    EasyDialerStatus.currentScreen = destination
}