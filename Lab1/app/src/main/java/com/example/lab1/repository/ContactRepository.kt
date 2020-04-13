package com.example.lab1.repository

import android.annotation.SuppressLint
import android.app.Application
import android.os.Build
import android.provider.ContactsContract
import com.example.lab1.data.db.ContactDao
import com.example.lab1.data.db.ContactDatabase
import com.example.lab1.model.Contact

class ContactRepository(application: Application) {
    private val contactDao: ContactDao
    
    init {
        val contactDatabase = ContactDatabase.getInstance(application)
        
        contactDao = contactDatabase.contactDao()
    }
    
    fun getAllContact(): List<Contact> {
        return contactDao.getAll()
    }
    
    fun insertContact(contact: Contact) {
        contactDao.insert(contact = contact)
    }
    
    fun findContact(id: Int): Contact {
        return contactDao.find(id = id)
    }
}