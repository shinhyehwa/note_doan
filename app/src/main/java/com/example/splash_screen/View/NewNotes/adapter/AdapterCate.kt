package com.example.splash_screen.View.NewNotes.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.splash_screen.R
import com.example.splash_screen.View.ScreenNote.`interface`.OnClickCategory
import com.example.splash_screen.model.category.Category


class AdapterCate (private val cateList: ArrayList<Category>): RecyclerView.Adapter<AdapterCate.ViewHolder>() {
    class ViewHolder(item: View) : RecyclerView.ViewHolder(item){
        val cateName: TextView = item.findViewById(R.id.category_name)
        val cateCheck: CheckBox = item.findViewById(R.id.category_checkBox)
    }

    var getIdCategory : OnClickCategory? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = cateList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cateName.text = cateList[position].categoryName
        holder.itemView.setOnClickListener {
            getIdCategory?.onGetIdCategory(cateList[position].categoryId)
        }
    }
}