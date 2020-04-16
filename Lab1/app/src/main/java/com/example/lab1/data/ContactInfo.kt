package com.example.lab1.data

import com.example.lab1.entity.Contact

val contact1 = Contact(
    id = 0,
    firstName = "Zhihui",
    lastName = "Xie",
    phoneNumber = "11111111111",
    email = "fffffarmer@gmail.com",
    github = "fffffarmer"
)

val contact2 = Contact(
    id = 1,
    firstName = "Travis",
    lastName = "Scott",
    phoneNumber = "120",
    email = "How can I know?"
)

val initialContacts = listOf(contact1, contact2)
