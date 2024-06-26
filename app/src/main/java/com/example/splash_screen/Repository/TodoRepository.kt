package com.example.splash_screen.Repository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.splash_screen.Model.Todos.TodoDatabase
import com.example.splash_screen.Model.Todos.Todos

class TodoRepository(private val db: TodoDatabase) {
    private var todoDatabase: TodoDatabase = db;

    fun addNewTodos(todos: ArrayList<Todos>) {
        try {
            todoDatabase.todoDatabase().addNewListToDo(todos)
        }catch (e: Exception){
            Log.e("Insert todos:", e.toString())
        }
    }

    fun addNewTodo(todos: Todos) {
        try {
            todoDatabase.todoDatabase().addNewToDo(todos)
        }catch (e: Exception){
            Log.e("Insert todo:", e.toString())
        }
    }

    fun getAllTodos(): LiveData<List<Todos>> {
        return try {
            val items = todoDatabase.todoDatabase().getAllToDo()
            items
        }catch (e: Exception){
            Log.e("get all todo:", e.toString())
            MutableLiveData<List<Todos>>().apply { value = emptyList() }
        }
    }

    fun deleteTodo(todo: Todos) {
        try {
            todoDatabase.todoDatabase().deleteToDo(todo)
        }catch (e: Exception){
            Log.e("delete todo", e.toString())
        }
    }
}