package com.example.splash_screen.model.category

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.splash_screen.model.Notes.Notes

@Dao
interface DaoCategory {
    @Query("SELECT * FROM category")
    fun getAllCategory(): LiveData<List<Category>>

    @Query("SELECT * FROM category")
    fun getAll(): List<Category>

    @Query("SELECT * FROM category WHERE categoryName = :name")
    fun getCateGoryByName(name : String): Category?

    @Insert
    fun addCategory(category: Category)

    @Insert
    fun addAllCategory(category: List<Category>)

    @Query("DELETE FROM category")
    fun deleteAllCategory()
}