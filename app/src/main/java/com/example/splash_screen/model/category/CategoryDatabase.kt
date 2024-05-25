package com.example.splash_screen.model.category

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Category::class], version = 3, exportSchema = false)
abstract class CategoryDatabase: RoomDatabase() {
    abstract fun categoryDatabase(): DaoCategory

    companion object{
        @Volatile
        private var Instance: CategoryDatabase? = null

        fun getDatabase(context: Context): CategoryDatabase{
            return Instance ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CategoryDatabase::class.java,
                    "category"
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()


                Instance = instance
                instance
            }
        }
    }
}