package com.example.splash_screen.model.Notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notes(
    @PrimaryKey(autoGenerate = true) val noteId: Int = 0,
    val title: String,
    val content: String,
    val dateCreate: Long,
    val dateUpdate: Long,
    val color: Int,
    val font: Int,
    val pin: Int,
    val categoryId: Int?,
    val imageByteArray: ByteArray?,
    val audioByteArray: ByteArray?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Notes

        if (imageByteArray != null) {
            if (other.imageByteArray == null) return false
            if (!imageByteArray.contentEquals(other.imageByteArray)) return false
        } else if (other.imageByteArray != null) return false
        if (audioByteArray != null) {
            if (other.audioByteArray == null) return false
            if (!audioByteArray.contentEquals(other.audioByteArray)) return false
        } else if (other.audioByteArray != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = imageByteArray?.contentHashCode() ?: 0
        result = 31 * result + (audioByteArray?.contentHashCode() ?: 0)
        return result
    }
}