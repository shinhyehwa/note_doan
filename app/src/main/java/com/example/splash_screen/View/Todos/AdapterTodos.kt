package com.example.splash_screen.View.Todos

import android.content.Context
import android.content.res.ColorStateList
import android.icu.util.Calendar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.splash_screen.model.Todos.Todos
import com.example.splash_screen.R
import com.example.splash_screen.ViewModel.TodoViewModel
import com.example.splash_screen.constans.enum_class.Priority
import com.example.splash_screen.constans.showTimeToNewNote


class AdapterTodos(private val todos: ArrayList<Todos>, private val context: Context, private val viewModel: TodoViewModel) :
    RecyclerView.Adapter<AdapterTodos.ViewHolder>() {
    class ViewHolder(itemsView: View) : RecyclerView.ViewHolder(itemsView) {
        var checkBox: CheckBox = itemsView.findViewById(R.id.todos_item_checkbox)
        val description: TextView = itemsView.findViewById(R.id.todos_item_description)
        val todoTime: TextView = itemsView.findViewById(R.id.todos_item_time)
    }

    var checkListener: OnClickCheckBox? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.toddos_rowitem, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = todos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = todos[position]
        holder.checkBox.buttonTintList = ColorStateList.valueOf(getColors(position))
        holder.checkBox.setTextColor(getColors(position))
        holder.todoTime.setTextColor(getColors(position))

        holder.checkBox.isChecked = isActive(data.active)
        holder.checkBox.text = data.title
        holder.todoTime.text = if (isActive(data.active)) data.finishDate?.let {
            showTimeToNewNote(
                it
            )
        } else showTimeToNewNote(data.createDate)
        if (data.description.toString().trim() != ""){
            val formattedDescription: String = context.getString(
                R.string.description_placeholder, data.description.toString()
            )
            holder.description.text = formattedDescription
            holder.description.visibility = View.VISIBLE
        }

        if (isActive(data.active)) {
            holder.checkBox.alpha = 0.5f
            holder.checkBox.paint.isStrikeThruText = true
        } else {
            holder.checkBox.alpha = 1f
            holder.checkBox.paint.isStrikeThruText = false
        }

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            run {
                if (isChecked) {
                    data.active = 1
                    data.finishDate = Calendar.getInstance().time.time
                    holder.checkBox.alpha = 0.5f
                    holder.checkBox.paint.isStrikeThruText = true
                } else {
                    data.active = 0
                    data.finishDate = null
                    holder.checkBox.alpha = 1f
                    holder.checkBox.paint.isStrikeThruText = false
                }
                checkListener?.updateActive(data)
                return@run
            }
        }


    }

    private fun getColors(position: Int): Int {
        return when (todos[position].priority) {
            Priority.HIGH -> ContextCompat.getColor(context, R.color.amour)
            Priority.LOW -> ContextCompat.getColor(context, R.color.jadeDust)
            else -> ContextCompat.getColor(context, R.color.doubleDragonSkin)
        }
    }

    private fun isActive(active: Int): Boolean {
        return active == 1
    }

    fun updateListTodo(todo: List<Todos>){
        val diffUtil = DiffUtil.calculateDiff(TodoDiffUtil(todo,todos.toList()))
        todos.clear()
        todos.addAll(todo)
        diffUtil.dispatchUpdatesTo(this)
    }
}