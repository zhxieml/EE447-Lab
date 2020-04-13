package com.example.lab1.data

import androidx.lifecycle.ViewModel
import com.example.lab1.model.Contact

//fun getContacts(): ArrayList<Contact> {
//    val projection = arrayOf(
//        ContactsContract.Profile._ID,
//        ContactsContract.Profile.DISPLAY_NAME_PRIMARY,
//        ContactsContract.Profile.LOOKUP_KEY,
//        ContactsContract.Profile.PHOTO_THUMBNAIL_URI
//    )
//}
class ContactInfoProvider {
    companion object {
        var contactList = initContactList()
        
        private fun initContactList(): MutableList<Contact> {
            val contact1 = Contact(
                phoneNumber = "123456",
                firstName = "Zhihui",
                lastName = "Xie"
            )

            val contact2 = Contact(
                phoneNumber = "21321432452",
                firstName = "Big",
                lastName = "Sean"
            )
            
            return mutableListOf<Contact>(contact1, contact2)
        }
    }
}
