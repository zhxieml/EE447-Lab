package com.example.lab1.model

import androidx.compose.Model
import androidx.room.Entity
import androidx.room.PrimaryKey

//@Model
//data class Contacts (
//    val contactNumber: Int,
//    val contacts: List<Contact> = emptyList()
//)

@Entity
data class Contact (
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var firstName: String = "",
    var lastName: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var github: String = ""
)