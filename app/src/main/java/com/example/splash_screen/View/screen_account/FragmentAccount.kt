package com.example.splash_screen.View.screen_account

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.splash_screen.R
import com.example.splash_screen.ViewModel.NoteViewModel
import com.example.splash_screen.ViewModel.TodoViewModel
import com.example.splash_screen.constans.Constant
import com.example.splash_screen.constans.enum_class.Priority
import com.example.splash_screen.model.NoteConverter
import com.example.splash_screen.model.Notes.Notes
import com.example.splash_screen.model.Todos.Todos
import com.example.splash_screen.model.category.Category
import com.example.splash_screen.model.dto.NoteDto
import com.google.android.gms.tasks.Tasks
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class FragmentAccount : Fragment() {
    private lateinit var auth: FirebaseAuth

    private lateinit var gmail: TextView
    private lateinit var image: ImageView
    private lateinit var userName: TextView

    private lateinit var logout: Button
    private lateinit var syncData: Button
    private lateinit var uploadData: Button
    private val dbNotes: NoteViewModel by lazy {
        ViewModelProvider(requireActivity())[NoteViewModel::class.java]
    }
    private val dbToDo: TodoViewModel by lazy {
        ViewModelProvider(requireActivity())[TodoViewModel::class.java]
    }
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        gmail = view.findViewById(R.id.MyAccount_gmail)
        image = view.findViewById(R.id.face_image)
        logout = view.findViewById(R.id.button)
        userName = view.findViewById(R.id.MyAccount_name)
        syncData = view.findViewById(R.id.sync_data)
        uploadData = view.findViewById(R.id.upload_data)


        val currentUser = auth.currentUser

        try {
            gmail.text = currentUser?.email
            userName.text = currentUser?.displayName
            Picasso.get().load(currentUser?.photoUrl).into(image)
        } catch (e: FirebaseException) {
            Log.e("exception", e.toString())
        }

        logout.setOnClickListener {
            auth.signOut()
            //replaceFragment(NoLoginFragment())
            Constant.selectedIndex = R.id.account
            requireActivity().recreate()
        }
        uploadData()
        syncData()
        return view
    }

    private fun showDialogNewCategory() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.new_category)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val title: TextView = dialog.findViewById(R.id.title)
        title.text = getString(R.string.create)
        val btnSave = dialog.findViewById<Button>(R.id.crate_cate_save)
        val btnClose = dialog.findViewById<Button>(R.id.create_cate_cancel)
            .setOnClickListener { dialog.dismiss() }
        val inputName = dialog.findViewById<TextInputEditText>(R.id.category_name)
        inputName.hint = "Tên bản lưu..."
        val inputLayout = dialog.findViewById<TextInputLayout>(R.id.category_layout)



        btnSave.setOnClickListener {
            val copyName: String = inputName.text.toString()
            val ref = db.collection(auth.uid.toString()).document(copyName)
            ref.get()
                .addOnSuccessListener {
                    if (it.exists()) {
                        inputLayout.helperText = "Bản ghi đã tồn tại!!"
                    } else {
                        setupDataUpLoad(copyName)
                        dialog.dismiss()
                    }
                }
        }

        dialog.show()
    }

    private fun uploadData() {
        uploadData.setOnClickListener {

            if (auth.currentUser == null) return@setOnClickListener

            showDialogNewCategory()

        }
    }

    private fun setupDataUpLoad(copyName: String) {
        val listNote: ArrayList<NoteDto> = ArrayList()
        val listToDo: ArrayList<Todos> = ArrayList()
        val listCategory: ArrayList<Category> = ArrayList()

        val currentId = auth.currentUser!!.uid
        listNote.addAll(dbNotes.getAllNote().map {
            NoteConverter.fromNoteToDto(it)
        })

        listCategory.addAll(dbNotes.getAllCate())
        listToDo.addAll(dbToDo.getAllTodo())

        val parentDocumentRef = db.collection(currentId).document(copyName)

        parentDocumentRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                for (note in listNote) {
                    parentDocumentRef.collection(Constant.NOTE).document(note.noteId.toString())
                        .set(note)
                }
                for (todo in listToDo) {
                    parentDocumentRef.collection(Constant.TODO).document(todo.TodoId.toString())
                        .set(todo)
                }
                for (category in listCategory) {
                    parentDocumentRef.collection(Constant.CATEGORY)
                        .document(category.categoryId.toString()).set(category)
                }
            } else {
                parentDocumentRef.set(hashMapOf<String, Any>()).addOnSuccessListener {
                    for (note in listNote) {
                        parentDocumentRef.collection(Constant.NOTE).document(note.noteId.toString())
                            .set(note)
                    }
                }

                parentDocumentRef.set(hashMapOf<String, Any>()).addOnCompleteListener {
                    for (todo in listToDo) {
                        parentDocumentRef.collection(Constant.TODO).document(todo.TodoId.toString())
                            .set(todo)
                    }
                }

                parentDocumentRef.set(hashMapOf<String, Any>()).addOnCompleteListener {
                    for (category in listCategory) {
                        parentDocumentRef.collection(Constant.CATEGORY)
                            .document(category.categoryId.toString()).set(category)
                    }
                }
            }

            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { exception ->
        }
    }


    private fun syncData() {
        syncData.setOnClickListener {
            if (auth.currentUser == null) return@setOnClickListener
            val collectionString = db.collection(auth.uid.toString())

            collectionString.get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val items: ArrayList<String> = ArrayList()
                    val document = it.result
                    for (copies in document) {
                        items.add(copies.id)
                    }
                    showAllBackups(items)
                }

            }
        }
    }

//    private fun showAllBackups(backups: List<String>) {
//        val dialog = BottomSheetDialog(requireContext())
//        val view = layoutInflater.inflate(R.layout.backup_layout, null)
//
//
//        val adapter = AdapterBackups(backups)
//        adapter.onItemClick = { documentId ->
//            val ref = db.collection(auth.uid.toString()).document(documentId)
//
//            ref.get().addOnSuccessListener {
//                val listNote: ArrayList<Notes> = ArrayList()
//                val listToDo: ArrayList<Todos> = ArrayList()
//                val listCategory: ArrayList<Category> = ArrayList()
//                if (it.exists()){
//                    ref.collection(Constant.CATEGORY).get().addOnSuccessListener{data  ->
//                        if (data != null){
//                            for (category in data){
//                                val res = Category(
//                                    categoryId = (category["categoryId"] as Long).toInt(),
//                                    categoryName = category["categoryName"] as String,
//                                    dateCreate = category["dateCreate"] as Long
//                                )
//                                listCategory.add(res)
//                            }
//                            Log.e("data", listCategory.toString())
//                        }
//                    }
//                    ref.collection(Constant.NOTE).get().addOnSuccessListener{data ->
//                        if (data != null){
//                            for (note in data){
//                                val res = NoteDto(
//                                    noteId = (note["noteId"] as Long).toInt(),
//                                    title = note["title"] as String,
//                                    content = note["content"] as String,
//                                    dateCreate = note["dateCreate"] as Long,
//                                    dateUpdate = note["dateUpdate"] as Long,
//                                    color = (note["color"] as Long).toInt(),
//                                    font = (note["font"] as Long).toInt(),
//                                    pin = (note["pin"] as Long).toInt(),
//                                    categoryId = (note["categoryId"] as? Long)?.toInt(),
//                                    imageByteArray = note["imageByteArray"] as? List<Int> ?: emptyList(),
//                                    audioByteArray = note["audioByteArray"] as? List<Int> ?: emptyList(),
//                                )
//                                listNote.add(NoteConverter.fromDtoToNote(res))
//                            }
//                            Log.e("data", listNote.toString())
//                        }
//                    }
//                    ref.collection(Constant.TODO).get().addOnSuccessListener{data ->
//                        if (data != null){
//                            for(todo in data){
//                                val res = Todos(
//                                    TodoId = (todo["todoId"] as Long).toInt(),
//                                    title =todo["title"] as String,
//                                    description = todo["description"] as? String,
//                                    active = (todo["active"] as Long).toInt(),
//                                    priority = Priority.valueOf(todo["priority"] as String),
//                                    createDate = todo["createDate"] as Long,
//                                    finishDate = todo["finishDate"] as? Long
//                                )
//                                listToDo.add(res)
//                            }
//                            Log.e("data", listToDo.toString())
//                        }
//                    }
//                    // check and add to room bb
//                }
//            }
//        }
//        val recyclerView: RecyclerView = view.findViewById(R.id.choose_backups)
//        recyclerView.adapter = adapter
//
//
//
//        dialog.setContentView(view)
//        dialog.show()
//    }

    private fun showAllBackups(backups: List<String>) {
        val backup = ArrayList<String>()
        backup.addAll(backups)
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.backup_layout, null)

        val adapter = AdapterBackups(backup)
        adapter.onItemClick = { documentId ->
            val ref = db.collection(auth.uid.toString()).document(documentId)

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val listCategory = withContext(Dispatchers.IO) { getCategoryData(ref) }
                    val listNote = withContext(Dispatchers.IO) { getNoteData(ref) }
                    val listToDo = withContext(Dispatchers.IO) { getTodoData(ref) }

                    dbNotes.insertAllNotes(listNote)
                    dbNotes.addAllCate(listCategory)
                    dbToDo.deleteAllTodo()
                    dbToDo.addTodos(listToDo)

                    dialog.dismiss()

                } catch (e: Exception) {
                    Log.e("Data Fetch Error", e.message ?: "Error fetching data")
                }
            }
        }
        adapter.getBackUpName = { backUpName ->
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Xác nhận xóa bản sao")
            builder.setMessage("Bạn có chắc chắn muốn xóa bản ghi $backUpName này không?")

            builder.setPositiveButton("Done") { dialog, _ ->
                db.collection(auth.uid.toString()).document(backUpName).delete()
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(),"Xóa thành công", Toast.LENGTH_SHORT).show()
                        backup.remove(backUpName)
                        adapter.notifyDataSetChanged()
                        dialog.dismiss()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(),"Xóa không thành công", Toast.LENGTH_SHORT).show()
                    }
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.choose_backups)
        recyclerView.adapter = adapter

        dialog.setContentView(view)
        dialog.show()
    }

    private suspend fun getCategoryData(ref: DocumentReference): List<Category> {
        val snapshot = ref.collection(Constant.CATEGORY).get().await()
        val listCategory = mutableListOf<Category>()
        for (category in snapshot) {
            val res = Category(
                categoryId = (category["categoryId"] as Long).toInt(),
                categoryName = category["categoryName"] as String,
                dateCreate = category["dateCreate"] as Long
            )
            listCategory.add(res)
        }
        return listCategory
    }

    private suspend fun getNoteData(ref: DocumentReference): List<Notes> {
        val snapshot = ref.collection(Constant.NOTE).get().await()
        val listNote = mutableListOf<Notes>()
        for (note in snapshot) {
            val res = NoteDto(
                noteId = (note["noteId"] as Long).toInt(),
                title = note["title"] as String,
                content = note["content"] as String,
                dateCreate = note["dateCreate"] as Long,
                dateUpdate = note["dateUpdate"] as Long,
                color = (note["color"] as Long).toInt(),
                font = (note["font"] as Long).toInt(),
                pin = (note["pin"] as Long).toInt(),
                categoryId = (note["categoryId"] as? Long)?.toInt(),
                imageByteArray = note["imageByteArray"] as? List<Int> ?: emptyList(),
                audioByteArray = note["audioByteArray"] as? List<Int> ?: emptyList()
            )
            listNote.add(NoteConverter.fromDtoToNote(res))
        }
        return listNote
    }

    private suspend fun getTodoData(ref: DocumentReference): List<Todos> {
        val snapshot = ref.collection(Constant.TODO).get().await()
        val listToDo = mutableListOf<Todos>()
        for (todo in snapshot) {
            val res = Todos(
                TodoId = (todo["todoId"] as Long).toInt(),
                title = todo["title"] as String,
                description = todo["description"] as? String,
                active = (todo["active"] as Long).toInt(),
                priority = Priority.valueOf(todo["priority"] as String),
                createDate = todo["createDate"] as Long,
                finishDate = todo["finishDate"] as? Long
            )
            listToDo.add(res)
        }
        return listToDo
    }

}