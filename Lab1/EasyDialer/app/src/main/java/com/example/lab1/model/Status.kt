package com.example.lab1.model

import androidx.compose.Model
import com.example.lab1.data.initialContacts

sealed class Screen {
    object Home: Screen()
}

@Model
object EasyDialerStatus {
    var currentScreen: Screen =
        Screen.Home
    var contacts = ArrayList<Contact>()
    var isCalling = false
    var phoneNumber = ""
    
    init {
        contacts.addAll(initialContacts)
    }
}

fun navigateTo(destination: Screen) {
    EasyDialerStatus.currentScreen = destination
}