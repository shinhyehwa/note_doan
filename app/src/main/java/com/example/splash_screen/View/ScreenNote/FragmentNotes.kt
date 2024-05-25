package com.example.splash_screen.View.ScreenNote

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.splash_screen.model.Notes.Notes
import com.example.splash_screen.R
import com.example.splash_screen.View.NewNotes.NewNotesFragment
import com.example.splash_screen.View.ScreenNote.`interface`.OnClickCategory
import com.example.splash_screen.ViewModel.NoteViewModel
import com.example.splash_screen.model.category.Category
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FragmentNotes : Fragment() {

    // variable
    // text view
    // image button
    private lateinit var imgBtnSearch: ImageButton

    // constrain layout
    private lateinit var searchView: ConstraintLayout

    // search view
    private lateinit var search: androidx.appcompat.widget.SearchView

    // button
    lateinit var btnCancel: Button

    // floating action button
    lateinit var btnAdd: FloatingActionButton

    // image view
    lateinit var backgroundImage: ImageView

    // card view
    lateinit var cardAllNotes : CardView

    // adapter
    lateinit var adapterNotes: AdapterNotes
    lateinit var adapterCategory: AdapterCategory

    // recyclerView
    lateinit var recyclerNote: RecyclerView
    lateinit var recyclerCategory: RecyclerView

    // variable
    private val listNotes: ArrayList<Notes> = ArrayList()
    private val listCategory: ArrayList<Category> = ArrayList()


    lateinit var dataHome: NoteViewModel
    private var searchJob: Job? = null
    private val debounceDuration = 1000L

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
        recyclerNote = view.findViewById(R.id.notes_recyclerview)
        recyclerCategory = view.findViewById(R.id.note_category)
        backgroundImage = view.findViewById(R.id.notes_noItem)
        btnAdd = view.findViewById(R.id.notes_fbtn_add)
        cardAllNotes = view.findViewById(R.id.all_note)

        dataHome = ViewModelProvider(this)[NoteViewModel::class.java]


        adapterNotes = AdapterNotes(listNotes, requireActivity().application)
        recyclerNote.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerNote.adapter = adapterNotes

        dataHome.items.observe(viewLifecycleOwner) {
            listNotes.clear()
            listNotes.addAll(it)
            resetAdapter(adapterNotes,recyclerNote)
            adapterNotes.notifyDataSetChanged()
            showBackGroundImage()
        }

        addCateGory()
        adapterCategory = AdapterCategory(listCategory, requireActivity().application)
        recyclerCategory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerCategory.adapter = adapterCategory
        //listener
        settingListener()
    }


    private fun settingListener() {
        imgBtnSearch.setOnClickListener(openSearchView())
        btnCancel.setOnClickListener(hidenSearchView())
        btnAdd.setOnClickListener(addNewNote())

        adapterNotes.onItemClick = {
            val fragmentManager = activity?.supportFragmentManager
            val fragmentTransaction = fragmentManager?.beginTransaction()
            val fragment = NewNotesFragment()

            val bundle = Bundle()
            bundle.putInt("noteId", it)
            fragment.arguments = bundle

            fragmentTransaction?.replace(android.R.id.content, fragment)
            fragmentTransaction?.commit()
            showBackGroundImage()
        }

        adapterNotes.notesListener = {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Xác nhận xóa ghi chú")
            builder.setMessage("Bạn có chắc chắn muốn xóa ghi chú này không?")

            builder.setPositiveButton("Done") { dialog, _ ->
                dataHome.selectNoteById(it)?.let { it1 -> dataHome.deleteNote(it1) }
                Toast.makeText(requireContext(),"Xóa thành công", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        adapterCategory.getCategoryId = object : OnClickCategory{
            override fun onGetIdCategory(categoryId: Int) {
                listNotes.clear()
                listNotes.addAll(dataHome.selectNoteByIdCategory(categoryId))
                val adapterNoteByCate = AdapterNotes(listNotes,requireActivity().application)
                recyclerNote.adapter = null
                recyclerNote.adapter = adapterNoteByCate
            }
        }
        searchNotes()

        cardAllNotes.setOnClickListener {
            dataHome.items.value?.let { items ->
                listNotes.clear()
                listNotes.addAll(items)
                recyclerNote.adapter = null
                recyclerNote.adapter = adapterNotes
                adapterNotes.notifyDataSetChanged()
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

    // search item
    private fun searchNotes(){
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if(listNotes.isEmpty()){
                    return false
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(listNotes.isEmpty()){
                    return false
                }
                searchJob?.cancel()
                searchJob = CoroutineScope(Dispatchers.Main).launch{
                    delay(debounceDuration)
                    val searchList = listNotes.filter {it.title.contains(p0.toString()) || it.content.contains(p0.toString())}
                    listNotes.clear()
                    listNotes.addAll(searchList)
                    resetAdapter(adapterNotes,recyclerNote)
                    adapterNotes.notifyDataSetChanged()
                }
                return false
            }

        })
    }

    private fun addCateGory(){
        dataHome.cates.observe(viewLifecycleOwner) {
            listCategory.clear()
            listCategory.addAll(it)
            resetAdapter(adapterCategory,recyclerCategory)
            adapterCategory.notifyDataSetChanged()
        }
    }

    private fun <A: RecyclerView.Adapter<*>> resetAdapter(adapter : A, recyclerView: RecyclerView){
        recyclerView.adapter = null
        recyclerView.adapter = adapter
    }

}