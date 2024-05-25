package com.example.splash_screen.View.NewNotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.splash_screen.R
import com.example.splash_screen.View.NewNotes.listener.OnClickItemPicker


class AdapterColors(private val colorList: List<Int>): RecyclerView.Adapter<AdapterColors.ViewHolder>() {
    class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        var viewColors: View = item.findViewById(R.id.card_colors)
    }

    var onClick: OnClickItemPicker? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.color_picker,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = colorList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.viewColors.setBackgroundColor(colorList[position])
        holder.itemView.setOnClickListener {
            onClick?.onClickPicker(colorList[position])
        }
    }
}