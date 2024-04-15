package com.example.splash_screen.View.ScreenNote

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.splash_screen.Model.Notes.Notes
import com.example.splash_screen.R
import com.example.splash_screen.View.NewNotes.NewNotesFragment
import com.example.splash_screen.ViewModel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class FragmentNotes : Fragment() {

    // variable
    // text view
    // image button
    lateinit var imgBtnSearch: ImageButton

    // constrain layout
    lateinit var searchView: ConstraintLayout

    // search view
    lateinit var search: androidx.appcompat.widget.SearchView

    // button
    lateinit var btnCancel: Button

    // floating action button
    lateinit var btnAdd: FloatingActionButton

    // image view
    lateinit var backgroundImage: ImageView

    // adapter
    lateinit var adapterNotes: AdapterNotes

    // recyclerView
    lateinit var recyclerView: RecyclerView

    // variable
    private val listNotes: ArrayList<Notes> = ArrayList()

    lateinit var dataHome: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes, container, false)
        // install view
        initial(view)
        return view
    }

    private fun initial(view: View) {
        imgBtnSearch = view.findViewById(R.id.notes_btn_search)
        searchView = view.findViewById(R.id.notes_search_view)
        search = view.findViewById(R.id.search_item)
        btnCancel = view.findViewById(R.id.notes_btn_cancel)
        recyclerView = view.findViewById(R.id.notes_recyclerview)
        backgroundImage = view.findViewById(R.id.notes_noItem)
        btnAdd = view.findViewById(R.id.notes_fbtn_add)

        dataHome = ViewModelProvider(this)[NoteViewModel::class.java]
        dataHome.items.observe(viewLifecycleOwner, Observer {
            listNotes.clear()
            listNotes.addAll(it)
            adapterNotes.notifyDataSetChanged()
            showBackGroundImage()
        })

        adapterNotes = AdapterNotes(listNotes, requireActivity().application)
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapterNotes
        //listener
        settingListener()
    }


    private fun settingListener() {
        imgBtnSearch.setOnClickListener(openSearchView())
        btnCancel.setOnClickListener(hidenSearchView())
        btnAdd.setOnClickListener(addNewNote())

        adapterNotes.notesListener = object : NoteListener {
            override fun onDeleteNotes() {
                showBackGroundImage()
            }
        }
    }

    // listener floating action button
    private fun addNewNote(): View.OnClickListener {
        return View.OnClickListener {
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            val fragment = NewNotesFragment()
            fragmentTransaction?.replace(android.R.id.content, fragment)
            fragmentTransaction?.commit()
            showBackGroundImage()
        }
    }

    private fun showBackGroundImage() {
        if (listNotes.size == 0) {
            backgroundImage.visibility = View.VISIBLE
        } else {
            backgroundImage.visibility = View.GONE
        }
    }

    // show search view
    private fun openSearchView(): View.OnClickListener {
        return View.OnClickListener {
            searchView.visibility = View.VISIBLE
            if (search.requestFocus()) {
                context?.let { it1 -> getSystemService(it1, InputMethodManager::class.java) }
                    ?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    // hiden search view
    private fun hidenSearchView(): View.OnClickListener {
        return View.OnClickListener {
            searchView.visibility = View.GONE
            search.setQuery("", false)
            search.clearFocus()

        }
    }

}