package com.example.splash_screen.View.Todos

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splash_screen.model.Todos.Todos
import com.example.splash_screen.R
import com.example.splash_screen.ViewModel.TodoViewModel
import com.example.splash_screen.constans.enum_class.Priority
import com.example.splash_screen.constans.enum_class.SortBy
import com.example.splash_screen.constans.showTimeToNewNote
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar


class FragmentCheckList : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var adapterTodos: AdapterTodos
    private lateinit var btnSortBy: ImageButton
    private val todos: ArrayList<Todos> = ArrayList()

    private lateinit var todoViewModel: TodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_check_list, container, false)
        recyclerView = view.findViewById(R.id.todos_list)
        floatingActionButton = view.findViewById(R.id.todos_fbtn_add)
        btnSortBy = view.findViewById(R.id.Todos_btn_sortBy)
        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        adapterTodos = AdapterTodos(todos, requireContext(), todoViewModel)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapterTodos
        recyclerView.itemAnimator = null

        todoViewModel.items.observe(viewLifecycleOwner) {
            todos.clear()
            todos.addAll(it)
            todoViewModel.sortTodosByPriority(todos, SortBy.PRIORITY)
            resetAdapter()
            adapterTodos.updateListTodo(todos.toList())
        }


        floatingActionButton.setOnClickListener {
            displayDialogLayoutInsert()
        }
        btnSortBy.setOnClickListener {
            displayPopUpSortBy()
        }

        adapterTodos.checkListener = object  : OnClickCheckBox{
            override fun updateActive(data: Todos) {
                todoViewModel.updateTodo(data)
            }

        }

        return view
    }

    private fun displayDialogLayoutInsert() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.bottom_isnsert_todo)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setGravity(Gravity.BOTTOM)

        val title: EditText = dialog.findViewById(R.id.task_name)
        val body: EditText = dialog.findViewById(R.id.task_description)

        val spinner: Spinner = dialog.findViewById(R.id.task_spinner)
        setupSpinner(spinner)

        val textClock: TextView = dialog.findViewById(R.id.task_time)
        val currentTime = Calendar.getInstance().time
        textClock.text = showTimeToNewNote(currentTime.time)

        val btnClosed: Button = dialog.findViewById(R.id.task_closed)
        btnClosed.setOnClickListener { dialog.dismiss() }

        val btnSave: Button = dialog.findViewById(R.id.task_save)


        btnSave.setOnClickListener {
            val titleText = title.text.toString().trim()
            if (titleText.isNotEmpty()) {
                val todo = Todos(
                    title = titleText,
                    description = body.text?.toString(),
                    active = 0,
                    createDate = currentTime.time,
                    priority = when (spinner.selectedItem.toString()) {
                        "LOW" -> Priority.LOW
                        "MEDIUM" -> Priority.MEDIUM
                        "HIGH" -> Priority.HIGH
                        else -> Priority.MEDIUM
                    },
                    finishDate = null
                )
                todoViewModel.addTodo(todo)
                dialog.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please enter a title", Toast.LENGTH_SHORT).show()
            }
        }


        dialog.show()
    }
    private fun displayPopUpSortBy(){
        val popupMenu = PopupMenu(requireContext(), btnSortBy)
        popupMenu.menuInflater.inflate(R.menu.popup_sortby, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
            when (item?.itemId) {
                R.id.menu_sort_dateCreate -> {
                    todoViewModel.sortTodosByPriority(todos,SortBy.DATECREATE)
                    resetAdapter()
                    adapterTodos.updateListTodo(todos.toList())
                    true
                }
                R.id.menu_sort_dateFinish -> {
                    todoViewModel.sortTodosByPriority(todos,SortBy.FINISHDATE)
                    resetAdapter()
                    adapterTodos.updateListTodo(todos.toList())
                    true
                }
                R.id.menu_sort_priority -> {
                    todoViewModel.sortTodosByPriority(todos,SortBy.PRIORITY)
                    resetAdapter()
                    adapterTodos.updateListTodo(todos.toList())
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    // set up spinner
    private fun setupSpinner(spinner: Spinner) {
        val priorities = Priority.values().map { it.toString() }.toTypedArray()
        val adapter =
            ArrayAdapter(spinner.context, android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.setSelection(1)
    }

    private fun resetAdapter(){
        recyclerView.adapter = null
        recyclerView.adapter = adapterTodos
    }
}