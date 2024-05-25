package com.example.splash_screen.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.splash_screen.model.Notes.Notes
import com.example.splash_screen.model.Notes.NotesDatabase

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

    fun getNoteById(ID: Int): Notes? {
        return try {
            val items = notesDatabase.noteDatabase().getNoteById(ID)
            items
        } catch (e: Exception) {
            Log.e("get all notes:", e.toString())
            null
        }
    }

    fun getNotesByIdCate(idCate: Int): List<Notes> {
        return try {
            val items = notesDatabase.noteDatabase().getNoteByIdCate(idCate)
            items
        } catch (e: Exception) {
            Log.e("error get notes by IdCate", e.toString())
            emptyList()
        }
    }

    fun searchNote(): List<Notes> {
        return emptyList()
    }

    fun deleteNote(data: Notes) {
        try {
            notesDatabase.noteDatabase().deleteNote(data)
        } catch (e: Exception) {
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

    fun insertAllNote(data: List<Notes>) {
        try {
            notesDatabase.noteDatabase().insertAllNotes(data)
        }catch (e: Exception){
            Log.e("insert notes: ", e.toString())
        }
    }

    fun deleteAllNotes(){
        try {
            notesDatabase.noteDatabase().deleteAllNotes()
        } catch (e: Exception) {
            Log.e("delete notes: ", e.toString())
        }
    }

    fun searchNoteByDateCreate(selectDay: Long, nextDay: Long): List<Notes> {
        return try {
            notesDatabase.noteDatabase().getNotesByDate(selectDay, nextDay)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun upDateNoteByNote(note: Notes) {
        try {
            notesDatabase.noteDatabase().updateNote(note)
        } catch (e: Exception) {
            Log.e("update notes: ", e.toString())
        }

    }
}