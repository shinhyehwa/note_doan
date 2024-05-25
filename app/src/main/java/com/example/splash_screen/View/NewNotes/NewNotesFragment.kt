package com.example.splash_screen.View.NewNotes

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.splash_screen.R
import com.example.splash_screen.View.NewNotes.adapter.AdapterCate
import com.example.splash_screen.View.NewNotes.adapter.AdapterColors
import com.example.splash_screen.View.NewNotes.adapter.AdapterFont
import com.example.splash_screen.View.NewNotes.listener.OnClickItemPicker
import com.example.splash_screen.View.ScreenNote.`interface`.OnClickCategory
import com.example.splash_screen.ViewModel.NoteViewModel
import com.example.splash_screen.constans.byteArrayToBitmap
import com.example.splash_screen.constans.Constant
import com.example.splash_screen.constans.Constant.Companion.REQUEST_CODE
import com.example.splash_screen.constans.showTimeToNewNote
import com.example.splash_screen.model.Notes.Notes
import com.example.splash_screen.model.category.Category
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.InputStream
import java.util.Date


class NewNotesFragment : Fragment() {
    private var permission = arrayOf(android.Manifest.permission.RECORD_AUDIO)
    private var permissionGranted = false
    // image button
    lateinit var btnBack: ImageButton
    lateinit var btnSave: ImageButton
    lateinit var btnBrush: ImageButton
    lateinit var btnImage: ImageButton
    lateinit var btnAudio: ImageButton
    lateinit var btnDeleteImage: ImageButton

    // Edit text
    lateinit var textTitle: EditText
    private lateinit var textContent: EditText

    // Text View
    lateinit var currentTime: TextView

    // card view
    lateinit var cardViewTool: CardView
    lateinit var cardImage: CardView
    lateinit var cardAudio: CardView

    // image view
    lateinit var noteImage: ImageView

    var isSave = false

    private var pathImage: ByteArray? = null
    private var fontFamily: Int? = null
    private var colorText: Int? = null

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var note: Notes
    private var noteId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            noteId = it.getInt("noteId", -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new_notes, container, false)
        noteViewModel = NoteViewModel(requireActivity().application)
        cardViewTool = view.findViewById(R.id.new_note_card_tool)
        cardImage = view.findViewById(R.id.NewNotes_cardImage)
        cardAudio = view.findViewById(R.id.NewNotes_cardAudio)

        textContent = view.findViewById(R.id.NewNotes_content)
        textTitle = view.findViewById(R.id.NewNotes_title)
        currentTime = view.findViewById(R.id.NewNotes_time)
        noteImage = view.findViewById(R.id.NewNotes_image)


        btnSave = view.findViewById(R.id.NewNotes_save)
        btnBack = view.findViewById(R.id.NewNotes_arrow_back)
        btnImage = view.findViewById(R.id.NewNotes_picture)
        btnAudio = view.findViewById(R.id.NewNotes_audio)
        btnBrush = view.findViewById(R.id.NewNotes_colors)
        btnDeleteImage = view.findViewById(R.id.NewNotes_delete_image)



        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
        if (noteId != -1) {
            noteViewModel = NoteViewModel(requireActivity().application)
            note = noteViewModel.selectNoteById(noteId)!!

            textContent.setText(note.content)
            textTitle.setText(note.title)
            currentTime.text = showTimeToNewNote(note.dateCreate)
            note.imageByteArray?.let {
                loadImageFromLocal(byteArrayToBitmap(it))
            }
        }
        initial()
        return view
    }

    @Deprecated("Deprecated in Java", ReplaceWith(
        "super.onRequestPermissionsResult(requestCode, permissions, grantResults)",
        "androidx.fragment.app.Fragment"
    )
    )
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE){
            permissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        }

    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            textTitle.clearFocus()
            textContent.clearFocus()
        }
    }

    private fun initial() {
        currentTime.text = showTimeToNewNote(Date().time)

        opacityButton(0.5f, false)

        changeContent()
        changeTitle()
        backToHome()
        saveNote()
        requestPermission() // recording
        getImageFromLocal()
        loadAudioFormLocal()
        deleteImage()
        changeColorAndFontNote()
    }

    private fun getData(categoryId: Int): Notes {
        return if (noteId == -1) {
            Notes(
                title = textTitle.text.toString(),
                content = textContent.text.toString(),
                color = colorText ?: Constant.colorDefault,
                dateCreate = Date().time,
                dateUpdate = Date().time,
                categoryId = categoryId,
                audioByteArray = null,
                imageByteArray = pathImage,
                pin = 0,
                font = fontFamily ?: Constant.fontDefault
            )
        } else {
            Notes(
                noteId = note.noteId,
                title = textTitle.text.toString(),
                content = textContent.text.toString(),
                color = colorText ?: Constant.colorDefault,
                dateCreate = note.dateCreate,
                dateUpdate = Date().time,
                categoryId = categoryId,
                audioByteArray = null,
                imageByteArray = pathImage,
                pin = 0,
                font = fontFamily ?: Constant.fontDefault
            )
        }
    }

    // enable button save
    private fun opacityButton(opacity: Float, enable: Boolean) {
        btnSave.alpha = opacity
        btnSave.isEnabled = enable
    }

    private fun changeContent() {
        val contentOld = textContent.text.toString().trim()
        textContent.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty() && contentOld == s.toString()) {
                    opacityButton(0.5f, false)
                    isSave = false
                    return
                }
                if (textTitle.text.isNotEmpty()) {
                    opacityButton(1f, true)
                    isSave = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun changeTitle() {
        val titleOld = textTitle.text.toString().trim()
        textTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty() && titleOld == s.toString()) {
                    opacityButton(0.5f, false)
                    isSave = false
                } else{
                    opacityButton(1f, true)
                    isSave = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun saveNote() {
        btnSave.setOnClickListener {
            if (noteId == -1) {
                val dialog = BottomSheetDialog(requireContext())
                val view = layoutInflater.inflate(R.layout.dialog_list_category, null)
                val btnDialogSave: Button = view.findViewById(R.id.choose_cate_save)
                val btnDialogCreateCate: Button = view.findViewById(R.id.choose_cate_create_cate)

                var idCategory: Int? = null

                btnDialogCreateCate.setOnClickListener {
                    showDialogNewCategory()
                }
                btnDialogSave.setOnClickListener {
                    if (idCategory != null) {
                        createRequestSave(idCategory)
                        Toast.makeText(requireContext(), "Lưu thành công", Toast.LENGTH_SHORT)
                            .show()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(requireContext(), "thất bại", Toast.LENGTH_SHORT).show()
                    }
                }

                val listCate: RecyclerView = view.findViewById(R.id.choose_cate_recycler)
                val listCategory: ArrayList<Category> = ArrayList()
                val adapterCate = AdapterCate(listCategory)
                adapterCate.getIdCategory = object : OnClickCategory {
                    override fun onGetIdCategory(categoryId: Int) {
                        idCategory = categoryId
                    }

                }
                noteViewModel.cates.observe(viewLifecycleOwner) {
                    listCategory.clear()
                    listCategory.addAll(it)
                    adapterCate.notifyDataSetChanged()
                }
                listCate.adapter = adapterCate



                dialog.setContentView(view)
                dialog.show()
            } else {
                noteViewModel.updateNote(getData(note.categoryId!!))
            }

        }
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

        val btnSave = dialog.findViewById<Button>(R.id.crate_cate_save)
        btnSave.isEnabled = false
        val btnClose = dialog.findViewById<Button>(R.id.create_cate_cancel)
            .setOnClickListener { dialog.dismiss() }
        val inputName = dialog.findViewById<TextInputEditText>(R.id.category_name)
        val inputLayout = dialog.findViewById<TextInputLayout>(R.id.category_layout)

        inputName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (noteViewModel.checkCate(p0.toString())) {
                    btnSave.isEnabled = true
                    inputLayout.helperText = null
                    inputLayout.boxStrokeColor = getColor(requireContext(), R.color.joust_blur)
                } else {
                    btnSave.isEnabled = false
                    inputLayout.helperText = "Thư mực đã tồn tại!!"
                    inputLayout.boxStrokeColor = getColor(requireContext(), R.color.amour)
                }
            }

        })

        btnSave.setOnClickListener {
            noteViewModel.addCategory(
                Category(
                    categoryName = inputName.text.toString(),
                    dateCreate = Date().time
                )
            )
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun createRequestSave(categoryId: Int?) {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(btnSave.windowToken, 0)
        textTitle.clearFocus()
        textContent.clearFocus()
        NoteViewModel(requireActivity().application).addNote(getData(categoryId ?: -1))
        isSave = false
    }

    private fun backToHome() {
        btnBack.setOnClickListener {
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.remove(this)
            fragmentTransaction.commit()
            fragmentManager.popBackStack()
        }
    }

    // image
    private fun getImageFromLocal(){
        btnImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
    }

    @SuppressLint("Recycle")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            data?.data?.let {
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(it)
                val imageByteArray = inputStream?.readBytes()
                if (imageByteArray != null) {
                    pathImage = imageByteArray
                    loadImageFromLocal(byteArrayToBitmap(imageByteArray))
                }

            }
        }
    }

    private fun loadImageFromLocal(image: Bitmap) {
        noteImage.setImageBitmap(image)
        cardImage.visibility = View.VISIBLE
    }

    private fun deleteImage() {
        btnDeleteImage.setOnClickListener {
            pathImage = null
            cardImage.visibility = View.GONE
        }
    }

    // audio
    private fun loadAudioFormLocal() {
        cardAudio.visibility = View.GONE
    }

    private fun requestPermission() {
        btnAudio.setOnClickListener {
            permissionGranted = ActivityCompat.checkSelfPermission(
                requireContext(),
                permission[0]
            ) == PackageManager.PERMISSION_GRANTED
            if (!permissionGranted) {
                ActivityCompat.requestPermissions(this.requireActivity(), permission, REQUEST_CODE)
            }

            startRecording()
        }
    }

    private fun startRecording() {
        if (!permissionGranted) {
            ActivityCompat.requestPermissions(this.requireActivity(), permission, REQUEST_CODE)
            return
        }
        // start recording
        Log.e("recording:", " alo alo ")
    }

    // brush
    private fun changeColorAndFontNote() {
        btnBrush.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.new_note_bottom_sheet, null)

            val colorPicker: RecyclerView = view.findViewById(R.id.list_colors)
            val fontPicker: RecyclerView = view.findViewById(R.id.list_font)
            val btnClose: Button = view.findViewById(R.id.bottom_sheet_close)

            val adapterColor = AdapterColors(Constant.colorList)
            colorPicker.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            colorPicker.adapter = adapterColor

            val adapterFont = AdapterFont(Constant.fontList, requireContext())
            fontPicker.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            fontPicker.adapter = adapterFont

            adapterColor.onClick = object : OnClickItemPicker {
                override fun onClickPicker(data: Int) {
                    setTextColors(data)
                }
            }
            adapterFont.onClick = object : OnClickItemPicker {
                override fun onClickPicker(data: Int) {
                    setFontText(data)
                }
            }

            btnClose.setOnClickListener { dialog.dismiss() }
            dialog.setContentView(view)
            dialog.setCancelable(false)
            dialog.show()
        }
    }

    private fun setTextColors(color: Int) {
        currentTime.setTextColor(color)
        textContent.setTextColor(color)
        textTitle.setTextColor(color)
        colorText = color
    }

    private fun setFontText(font: Int) {
        val typeface = ResourcesCompat.getFont(requireContext(), font)
        currentTime.typeface = typeface
        textContent.typeface = typeface
        textTitle.typeface = typeface
        fontFamily = font
    }


}