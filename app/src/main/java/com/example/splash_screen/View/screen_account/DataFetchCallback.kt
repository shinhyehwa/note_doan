package com.example.splash_screen.View.screen_account

import com.example.splash_screen.model.Notes.Notes
import com.example.splash_screen.model.Todos.Todos
import com.example.splash_screen.model.category.Category

interface DataFetchCallback {
    fun onDataFetched(listCategory: List<Category>, listNote: List<Notes>, listToDo: List<Todos>)
}
