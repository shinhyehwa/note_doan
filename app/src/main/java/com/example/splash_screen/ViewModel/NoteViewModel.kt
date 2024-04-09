package com.example.splash_screen.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.splash_screen.Model.Notes.Notes
import com.example.splash_screen.Model.Notes.NotesDatabase
import com.example.splash_screen.Repository.NoteRepository

class NoteViewModel(application: Application): AndroidViewModel(application){
    private val noteDb: NotesDatabase = NotesDatabase.getDatabase(application)
    private val noteRepository = NoteRepository(noteDb)

    val items = noteRepository.getAllNotes();

    fun addNote(note : Notes){
        noteRepository.insertNote(note)
    }

    fun deleteNote(note: Notes) {
        noteRepository.deleteNote(note)
    }
}