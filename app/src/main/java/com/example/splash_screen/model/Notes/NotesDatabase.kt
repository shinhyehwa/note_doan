package com.example.splash_screen.model.Notes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.splash_screen.constans.Constant

@Database(entities = [Notes::class], version = 2, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun noteDatabase(): DaoNotes

    companion object {
        @Volatile
        private var Instance: NotesDatabase? = null

        fun getDatabase(context: Context): NotesDatabase {
            return Instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDatabase::class.java,
                    Constant.NAME_DATABASE
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()

                Instance = instance
                instance
            }
        }
    }
}