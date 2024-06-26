package com.example.splash_screen.Model.Todos

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.splash_screen.constans.const

@Database(entities = [Todos::class], version = 1)
abstract class TodoDatabase : RoomDatabase(){
    abstract fun todoDatabase(): DaoTodo

    companion object{
        @Volatile
        private var Instance: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            return Instance ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    const.TODO_DATABASE
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()

                Instance = instance
                instance
            }
        }
    }
}