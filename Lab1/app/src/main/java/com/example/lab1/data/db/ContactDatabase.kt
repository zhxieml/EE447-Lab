package com.example.lab1.data.db

import android.app.Application
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.lab1.data.ContactInfoProvider
import com.example.lab1.model.Contact

@Database(entities = [Contact::class], version = 1)
abstract class ContactDatabase: RoomDatabase() {
    abstract fun contactDao(): ContactDao
    
    companion object {
        private val lock = Any()
        private const val DB_NAME = "Contact.db"
        private var INSTANCE: ContactDatabase? = null

        fun getInstance(application: Application): ContactDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        application,
                        ContactDatabase::class.java,
                        DB_NAME
                    )
                        .allowMainThreadQueries()
                        .addCallback(
                            object : RoomDatabase.Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                    ContactDatabase.INSTANCE?.let { 
                                        ContactDatabase.prePopulate(
                                            it,
                                            ContactInfoProvider.contactList
                                        )
                                    }
                                }
                            }
                        )
                        .build()
                }
            }

            return INSTANCE!!
        }
        
        fun prePopulate(database: ContactDatabase, contactList: List<Contact>) {
            for (contact in contactList) {
                AsyncTask.execute {
                    database.contactDao().insert(contact = contact)
                }
            }
        }
    }
}