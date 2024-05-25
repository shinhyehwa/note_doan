package com.example.splash_screen.model.Notes

import android.util.Log
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

    @Query("SELECT * FROM notes")
    fun getAll(): List<Notes>

    // get data by id
    @Query("SELECT * FROM notes WHERE noteId = :Id")
    fun getNoteById(Id: Int): Notes

    // insert data
    @Insert
    fun insertNote(item: Notes)

    @Insert
    fun insertAllNotes(notes: List<Notes>)

    @Query("DELETE FROM Notes")
    fun deleteAllNotes()

    // delete data by id
    @Delete
    fun deleteNote(item: Notes)

    //update data
    @Update
    fun updateNote(item: Notes)

    // search note by dateCreate
    @Query("SELECT * FROM Notes WHERE dateCreate >= :selectedDateWithZeroTime AND dateCreate < :nextDayWithZeroTime")
    fun getNotesByDate(selectedDateWithZeroTime: Long, nextDayWithZeroTime: Long): List<Notes>

    @Query("SELECT * FROM Notes WHERE categoryId = :idCate")
    fun getNoteByIdCate(idCate:Int) : List<Notes>
}