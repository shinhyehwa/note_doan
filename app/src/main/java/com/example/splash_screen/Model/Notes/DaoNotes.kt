package com.example.splash_screen.Model.Notes

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoNotes {
    // fetch all data
    @Query("SELECT * FROM notes")
    fun getAllNotes(): LiveData<List<Notes>>

    // get data by id
    @Query("SELECT * FROM notes WHERE noteId = :Id")
    fun getNoteById(Id: Int): LiveData<Notes>

    // insert data
    @Insert
    fun insertNote(item: Notes)

    // delete data by id
    @Delete
    fun deleteNote(item: Notes)

    //update data
    @Update
    fun updateNote(item: Notes)
}