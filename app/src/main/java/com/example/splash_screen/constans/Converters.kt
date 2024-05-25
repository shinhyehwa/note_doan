package com.example.splash_screen.model

import com.example.splash_screen.model.Notes.Notes
import com.example.splash_screen.model.dto.NoteDto

object NoteConverter {
    fun fromNoteToDto(note: Notes): NoteDto {
        return NoteDto(
            noteId = note.noteId,
            title = note.title,
            content = note.content,
            dateCreate = note.dateCreate,
            dateUpdate = note.dateUpdate,
            color = note.color,
            font = note.font,
            pin = note.pin,
            categoryId = note.categoryId,
            imageByteArray = note.imageByteArray?.map { it.toInt() },
            audioByteArray = note.audioByteArray?.map { it.toInt() }
        )
    }

    fun fromDtoToNote(noteDto: NoteDto): Notes {
        return Notes(
            noteId = noteDto.noteId,
            title = noteDto.title,
            content = noteDto.content,
            dateCreate = noteDto.dateCreate,
            dateUpdate = noteDto.dateUpdate,
            color = noteDto.color,
            font = noteDto.font,
            pin = noteDto.pin,
            categoryId = noteDto.categoryId,
            imageByteArray = noteDto.imageByteArray?.map { it.toByte() }?.toByteArray(),
            audioByteArray = noteDto.audioByteArray?.map { it.toByte() }?.toByteArray()
        )
    }
}

