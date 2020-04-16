package com.example.lab1.data

import android.Manifest

const val appTitle = "Easy Dialer"
val permissionsNeeded = arrayListOf<String>(
    Manifest.permission.CALL_PHONE,
    Manifest.permission.READ_CONTACTS,
    Manifest.permission.WRITE_CONTACTS
)