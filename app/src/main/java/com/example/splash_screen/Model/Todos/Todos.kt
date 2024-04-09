package com.example.splash_screen.Model.Todos

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.splash_screen.constans.const

@Entity(tableName = const.TODO_DATABASE)
data class Todos(
    @PrimaryKey(autoGenerate = true) val TodoId: Int,
    val title: String,
    val active: Int,
    val createDate: Long,
    val finishDate: Long
)