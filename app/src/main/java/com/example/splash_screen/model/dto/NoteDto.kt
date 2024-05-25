package com.example.splash_screen.model.dto

data class NoteDto(
    val noteId: Int,
    val title: String,
    val content: String,
    val dateCreate: Long,
    val dateUpdate: Long,
    val color: Int,
    val font: Int,
    val pin: Int,
    val categoryId: Int?,
    val imageByteArray: List<Int>?,
    val audioByteArray: List<Int>?,
)