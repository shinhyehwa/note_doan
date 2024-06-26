package com.example.splash_screen.Model.Todos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DaoTodo {

    // insert data
    @Insert
    fun addNewToDo(todo: Todos)

    @Insert
    fun addNewListToDo(data: List<Todos>)

    // delete Todos
    @Delete
    fun deleteToDo(todo: Todos)

    // update Todo
    @Update
    fun updateToDo(todo: Todos)

    // fetch all data
    @Query("SELECT * FROM ToDo_Database")
    fun getAllToDo(): LiveData<List<Todos>>
}