package com.example.splash_screen.View.calander.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.splash_screen.Model.Notes.Notes
import com.example.splash_screen.R
import com.example.splash_screen.constans.byteArrayToBitmap
import com.example.splash_screen.constans.showTimeToNewNote

class AdapterCalender(private val items: ArrayList<Notes>,) : RecyclerView.Adapter<AdapterCalender.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val edtTitle: TextView = itemView.findViewById(R.id.NoteCalendar_title)
        val edtContents: TextView = itemView.findViewById(R.id.NoteCalendar_content)
        val txtNoteTime: TextView = itemView.findViewById(R.id.NoteCalendar_time)
        val image: ImageView = itemView.findViewById(R.id.NoteCalendar_image)
        val imageLayout: CardView = itemView.findViewById(R.id.NoteCalendar_imageLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_row_calendar, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]
        holder.edtTitle.text = data.title
        holder.edtContents.text = data.content
        holder.txtNoteTime.text = showTimeToNewNote(data.dateCreate)

        if(data.imageByteArray != null){
            try {
                holder.image.setImageBitmap(byteArrayToBitmap(data.imageByteArray))
                holder.imageLayout.visibility = View.VISIBLE
            }catch (e: Exception) {
                Log.e("error load", e.toString())
            }
        }

        holder.itemView.setOnClickListener {
            Log.e("id notes", data.noteId.toString())
        }
    }
}