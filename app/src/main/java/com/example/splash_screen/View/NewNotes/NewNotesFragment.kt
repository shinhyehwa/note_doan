package com.example.splash_screen.View.NewNotes

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.splash_screen.Model.Notes.Notes
import com.example.splash_screen.R
import com.example.splash_screen.ViewModel.NoteViewModel
import com.example.splash_screen.constans.byteArrayToBitmap
import com.example.splash_screen.constans.const.Companion.REQUEST_CODE
import com.example.splash_screen.constans.showTimeToNewNote
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

    // Edit text
    lateinit var textTitle: EditText
    private lateinit var textContent: EditText

    // Text View
    lateinit var currentTime: TextView

    // card view
    lateinit var cardViewTool: CardView
    lateinit var cardImage: CardView

    // image view
    lateinit var noteImage: ImageView

    var isFocus = false;
    var isSave = false

    private var pathImage: ByteArray ? =  null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new_notes, container, false)
        cardViewTool = view.findViewById(R.id.new_note_card_tool)
        cardImage = view.findViewById(R.id.NewNotes_cardImage)

        textContent = view.findViewById(R.id.NewNotes_content)
        textTitle = view.findViewById(R.id.NewNotes_title)
        currentTime = view.findViewById(R.id.NewNotes_time)
        noteImage = view.findViewById(R.id.NewNotes_image)


        btnSave = view.findViewById(R.id.NewNotes_save)
        btnBack = view.findViewById(R.id.NewNotes_arrow_back)
        btnImage = view.findViewById(R.id.NewNotes_picture)
        btnAudio = view.findViewById(R.id.NewNotes_audio)



        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
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

    fun initial() {
        textTitle.setOnFocusChangeListener { _, focus ->
            isFocus = focus
            if (focus) {
                cardViewTool.visibility = View.GONE
            }
        }
        textContent.setOnFocusChangeListener { _, focus ->
            isFocus = focus
            if (focus) {
                cardViewTool.visibility = View.GONE
            }
        }
        currentTime.text = showTimeToNewNote(Date().time)

        opacityButton(0.5f, false)

        changeContent()
        changeTitle()
        backToHome()
        saveNote()
        requestPermission() // recording
        getImageFromLocal()
    }

    private fun getData(): Notes {
        return Notes(
            title = textTitle.text.toString(),
            content = textContent.text.toString(),
            color = Color.RED,
            dateCreate = Date().time,
            dateUpdate = Date().time,
            audioByteArray = null,
            imageByteArray = pathImage,
        )
    }

    private fun checkValidation(): Boolean {
        return checkContent() && checkTitle()
    }

    private fun checkTitle(): Boolean {
        return textTitle.text.toString().trim().isNotBlank()
    }

    private fun checkContent(): Boolean {
        return textContent.text.toString().trim().isNotBlank()
    }

    // enable button save
    private fun opacityButton(opac: Float, enable : Boolean) {
        btnSave.alpha = opac
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
                } else{
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

    private fun saveNote(){
        btnSave.setOnClickListener {
            if (isFocus) {
                val inputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
                cardViewTool.visibility = View.VISIBLE
                textTitle.clearFocus()
                textContent.clearFocus()
            }
            if (checkValidation()) {
                NoteViewModel(requireActivity().application).addNote(getData())
                isSave = false
            }
        }
    }

    private fun backToHome(){
        btnBack.setOnClickListener {
            if(isSave){
                Toast.makeText(context,"chưa lưu thay đổi", Toast.LENGTH_SHORT).show()
            }else{
                val fragmentManager = requireActivity().supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.remove(this)
                fragmentTransaction.commit()
                fragmentManager.popBackStack()
            }
        }
    }

    private fun getImageFromLocal(){
        btnImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1){
            data?.data?.let {
                val inputStream: InputStream? = requireContext().contentResolver.openInputStream(it)
                val imageByteArray = inputStream?.readBytes()
                if (imageByteArray != null) {
                    pathImage = imageByteArray
                    loadImageFromUri(byteArrayToBitmap(imageByteArray))
                }

            }
        }
    }

    private fun loadImageFromUri(image: Bitmap) {
        noteImage.setImageBitmap(image)
        cardImage.visibility = View.VISIBLE
    }

    private fun requestPermission(){
        btnAudio.setOnClickListener {
            permissionGranted = ActivityCompat.checkSelfPermission(requireContext(), permission[0]) == PackageManager.PERMISSION_GRANTED
            if(!permissionGranted){
                ActivityCompat.requestPermissions(this.requireActivity(),permission, REQUEST_CODE)
            }

            startRecording()
        }
    }

    private fun startRecording(){
        if(!permissionGranted){
            ActivityCompat.requestPermissions(this.requireActivity(), permission, REQUEST_CODE)
            return
        }
        // start recording
        Log.e("recording:", " alo alo ")
    }

}