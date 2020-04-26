package com.example.locateme.model

data class Fingerprint(
    val location: Location,
    val accessPoints: List<AccessPoint>
)