package com.example.splash_screen.View.NewNotes.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.splash_screen.R
import com.example.splash_screen.View.NewNotes.listener.OnClickItemPicker

class AdapterFont(private val fontList: List<Int>, private val context: Context): RecyclerView.Adapter<AdapterFont.ViewHolder>(){
    class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        var fontPicker: TextView = item.findViewById(R.id.font_picker)
    }

    var onClick: OnClickItemPicker? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.font_picker, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = fontList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fontResourceId = fontList[position]
        val typeface = ResourcesCompat.getFont(context, fontResourceId)
        holder.fontPicker.typeface = typeface
        holder.itemView.setOnClickListener {
            onClick?.onClickPicker(fontResourceId)
        }
    }
}