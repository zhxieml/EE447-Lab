package com.example.locateme.model

data class AccessPoint(
    var BSSID: String = "",
    var SSID: String = "",
    var strength: Int = 0
)