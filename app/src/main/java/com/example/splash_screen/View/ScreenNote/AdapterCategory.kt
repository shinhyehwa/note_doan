package com.example.splash_screen.View.ScreenNote

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.splash_screen.R
import com.example.splash_screen.View.ScreenNote.`interface`.OnClickCategory
import com.example.splash_screen.model.category.Category

class AdapterCategory(private val category: ArrayList<Category>, private val context: Application) :
    RecyclerView.Adapter<AdapterCategory.ViewHolder>() {

    var getCategoryId: OnClickCategory? = null

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val categoryName: TextView = item.findViewById(R.id.category_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = category.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.categoryName.text = category[position].categoryName
        holder.itemView.setOnClickListener {
            getCategoryId?.onGetIdCategory(categoryId = category[position].categoryId)
        }
    }

}