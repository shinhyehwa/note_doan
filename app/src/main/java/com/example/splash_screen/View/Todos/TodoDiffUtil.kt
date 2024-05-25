package com.example.splash_screen.View.Todos

import androidx.recyclerview.widget.DiffUtil
import com.example.splash_screen.model.Todos.Todos

class TodoDiffUtil(val newList: List<Todos>,val oldList: List<Todos>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return  oldList.size
    }

    override fun getNewListSize(): Int {
        return  newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].TodoId == newList[newItemPosition].TodoId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].createDate == newList[newItemPosition].createDate -> false
            oldList[oldItemPosition].title == newList[newItemPosition].title -> false
            oldList[oldItemPosition].active == newList[newItemPosition].active -> false
            oldList[oldItemPosition].finishDate == newList[newItemPosition].finishDate -> false
            oldList[oldItemPosition].priority == newList[newItemPosition].priority -> false
            else -> true
        }
    }

}