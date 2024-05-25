package com.example.splash_screen.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.splash_screen.model.Notes.Notes
import com.example.splash_screen.model.Notes.NotesDatabase
import com.example.splash_screen.Repository.NoteRepository
import com.example.splash_screen.model.category.Category
import com.example.splash_screen.model.category.CategoryDatabase
import java.lang.Exception
import java.util.Calendar

class NoteViewModel(application: Application): AndroidViewModel(application){
    private val noteDb: NotesDatabase = NotesDatabase.getDatabase(application)
    private val cateDb: CategoryDatabase = CategoryDatabase.getDatabase(application)
    private val noteRepository = NoteRepository(noteDb)

    val items = noteRepository.getAllNotes();
    val cates = cateDb.categoryDatabase().getAllCategory()

    fun addNote(note : Notes){
        noteRepository.insertNote(note)
    }

    fun addCategory(category: Category){
        try {
            cateDb.categoryDatabase().addCategory(category)
        }catch (e:Exception){
            Log.e("error cate", e.toString())
        }
    }

    fun getAllCate(): List<Category>{
        return cateDb.categoryDatabase().getAll()
    }

    fun getAllNote(): List<Notes>{
        return noteDb.noteDatabase().getAll()
    }
    fun deleteNote(note: Notes) {
        noteRepository.deleteNote(note)
    }

    fun insertAllNotes(notes: List<Notes>){
        deleteAllNote()
        noteRepository.insertAllNote(notes)
    }

    private fun deleteAllNote(){
        noteRepository.deleteAllNotes()
    }
    fun addAllCate(cate: List<Category>){
        cateDb.categoryDatabase().deleteAllCategory()
        cateDb.categoryDatabase().addAllCategory(cate)
    }

    fun selectNoteByDay(selectDay: Long): List<Notes>{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectDay
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val nextDay = calendar.timeInMillis
        return noteRepository.searchNoteByDateCreate(selectDay, nextDay)
    }

    fun selectNoteById(noteId: Int): Notes? {
        return noteRepository.getNoteById(noteId)
    }
    fun selectNoteByIdCategory(idCategory : Int) : List<Notes>{
        return  noteRepository.getNotesByIdCate(idCategory)
    }
    fun checkCate(name: String): Boolean {
        val data = cateDb.categoryDatabase().getCateGoryByName(name) ?: return true
        return false
    }

    fun updateNote(note: Notes){
        noteRepository.upDateNoteByNote(note)
    }

}