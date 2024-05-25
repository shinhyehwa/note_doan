package com.example.splash_screen.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.splash_screen.model.Todos.TodoDatabase
import com.example.splash_screen.model.Todos.Todos
import com.example.splash_screen.Repository.TodoRepository
import com.example.splash_screen.constans.enum_class.SortBy

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDb: TodoDatabase = TodoDatabase.getDatabase(application)
    private val todoRepository: TodoRepository = TodoRepository(todoDb)

    val items = todoRepository.getAllTodos();

    fun addTodo(todo: Todos) {
        todoRepository.addNewTodo(todo)
    }

    fun addTodos(todo: List<Todos>) {
        todoRepository.addNewTodos(todo)
    }

    fun getAllTodo(): List<Todos>{
        return todoDb.todoDatabase().getAll()
    }

    fun deleteTodo(todo: Todos) {
        todoRepository.deleteTodo(todo)
    }

    fun deleteAllTodo(){
        todoRepository.deleteAllTodos()
    }


    fun updateTodo(todo: Todos){
        todoRepository.updateTodo(todo)
    }
    fun sortTodosByPriority(todos: ArrayList<Todos>, sortBy: SortBy) {
        when (sortBy) {
            SortBy.PRIORITY -> {
                val sorted = todos.partition { it.active == 0 }
                val activeTodos = sorted.second
                val nonActiveTodos = sorted.first
                val sortedNonActive = nonActiveTodos.sortedWith(compareByDescending { it.priority })
                val sortedActive = activeTodos.sortedWith(compareByDescending { it.priority })
                val sortedList = sortedNonActive + sortedActive
                todos.clear()
                todos.addAll(sortedList)
            }

            SortBy.DATECREATE -> {
                val sorted = todos.sortedBy { it -> it.createDate }
                todos.clear()
                todos.addAll(sorted)
            }

            else -> {
                val sorted = todos.sortedBy { it -> it.finishDate }
                todos.clear()
                todos.addAll(sorted)
            }
        }
    }


}