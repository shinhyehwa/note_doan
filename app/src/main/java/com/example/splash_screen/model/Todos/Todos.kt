package com.example.splash_screen.model.Todos

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.splash_screen.constans.enum_class.Priority
import com.example.splash_screen.constans.Constant

@Entity(tableName = Constant.TODO_DATABASE)
data class Todos(
    @PrimaryKey(autoGenerate = true) val TodoId: Int = 0,
    val title: String,
    val description: String?,
    var active: Int,
    val priority: Priority = Priority.MEDIUM,
    val createDate: Long,
    var finishDate: Long?
)
