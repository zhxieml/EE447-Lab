package com.example.lab1.data.db

import androidx.compose.Composable
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab1.model.Contact

@Dao
interface ContactDao {
    @Query("SELECT * FROM Contact ORDER BY id DESC")
    fun getAll(): List<Contact>

    @Query("DELETE FROM Contact")
    fun deleteAll()
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact)
    
    @Query("SELECT * FROM Contact WHERE id = :id")
    fun find(id: Int): Contact
}