package com.example.splash_screen.constans

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import com.example.splash_screen.R

class CustomDialogError(context: Context, private val imageResId: Int?, private val message: String) : Dialog(context) {
    private val dismissHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_error)

        // Set image
        if (imageResId != null){
            val imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageResource(imageResId)
        }

        // Set message
        val messageTextView = findViewById<TextView>(R.id.messageTextView)
        messageTextView.text = message

        dismissHandler.postDelayed({
            dismiss()
        }, 3000)
    }

    override fun onStop() {
        super.onStop()
        dismissHandler.removeCallbacksAndMessages(null)
    }
}
