package com.example.splash_screen.View.ScreenNote

import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.example.splash_screen.Model.Notes.Notes
import com.example.splash_screen.R
import com.example.splash_screen.ViewModel.NoteViewModel
import com.example.splash_screen.constans.byteArrayToBitmap
import com.example.splash_screen.constans.showTimeToNewNote

class AdapterNotes(private val items: ArrayList<Notes>, private val context: Application) :
    RecyclerView.Adapter<AdapterNotes.ViewHolder>() {

    var notesListener: NoteListener? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val edtTitle: TextView = itemView.findViewById(R.id.title_notes)
        val edtContents: TextView = itemView.findViewById(R.id.note_content)
        val txtNoteTime: TextView = itemView.findViewById(R.id.note_time)
        val checkBox: ImageView = itemView.findViewById(R.id.delete_checkbox)
        val image: ImageView = itemView.findViewById(R.id.note_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.notes_rowitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.edtTitle.text = data.title
        holder.edtContents.text = data.content
        holder.txtNoteTime.text = showTimeToNewNote(data.dateUpdate)
        if (data.imageByteArray != null) {
            try {
                holder.image.setImageBitmap(byteArrayToBitmap(data.imageByteArray))
                holder.image.visibility = View.VISIBLE
            } catch (e: Exception) {
                Log.e("error load", e.toString())
            }
        }
        holder.itemView.setOnClickListener {
            deleteItemIndex(position)
        }

    }

    // delete item
    private fun deleteItemIndex(position: Int) {
        try {
            val data = items[position]
            NoteViewModel(context).deleteNote(data)
        } catch (e: Exception) {
            Log.e("delete note: ", e.toString())
        }

        if (items.size == 0) {
            notesListener?.onDeleteNotes()
        }
    }
}