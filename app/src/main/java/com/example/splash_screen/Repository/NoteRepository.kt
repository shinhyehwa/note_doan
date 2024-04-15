package com.example.splash_screen.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.splash_screen.Model.Notes.Notes
import com.example.splash_screen.Model.Notes.NotesDatabase
import kotlin.Exception

class NoteRepository(private val db: NotesDatabase){
    private var notesDatabase: NotesDatabase = db

    fun getAllNotes(): LiveData<List<Notes>> {
        return try {
            val items = notesDatabase.noteDatabase().getAllNotes()
            items
        }catch (e: Exception){
            Log.e("get all notes:", e.toString())
            MutableLiveData<List<Notes>>().apply { value = emptyList() }
        }
    }

    fun getNoteById(ID: Int): LiveData<Notes> {
        return try {
            val items = notesDatabase.noteDatabase().getNoteById(ID)
            items
        }catch (e: Exception){
            Log.e("get all notes:", e.toString())
            MutableLiveData<Notes>().apply { value = null }
        }
    }

    fun searchNote(): List<Notes> {
        return emptyList()
    }

    fun deleteNote(data: Notes) {
        try {
            notesDatabase.noteDatabase().deleteNote(data)
        } catch (e: Exception){
            Log.e("delete notes: ", e.toString())
        }
    }

    fun insertNote(data: Notes) {
        try {
            notesDatabase.noteDatabase().insertNote(data)
        }catch (e: Exception){
            Log.e("insert notes: ", e.toString())
        }
    }

    fun searchNoteByDateCreate(selectDay: Long,nextDay: Long): List<Notes>{
        return try {
            notesDatabase.noteDatabase().getNotesByDate(selectDay, nextDay)
        }catch (e: Exception){
            emptyList()
        }
    }
}