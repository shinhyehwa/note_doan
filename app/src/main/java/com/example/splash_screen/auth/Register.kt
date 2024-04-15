package com.example.splash_screen.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.splash_screen.Main
import com.example.splash_screen.R
import com.example.splash_screen.constans.validateEmail
import com.example.splash_screen.constans.validatePassword
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException


class Register : AppCompatActivity() {
    private lateinit var btnRegister: Button
    private lateinit var btnBack: Button

    private lateinit var gmailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmPassLayout: TextInputLayout

    private lateinit var gmail: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirmPassword: TextInputEditText

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        initial()
    }

    private fun initial() {
        btnBack = findViewById(R.id.register_btn_back)
        btnRegister = findViewById(R.id.register_btn)

        gmailLayout = findViewById(R.id.register_input_email_layout)
        gmail = findViewById(R.id.register_input_email)

        passwordLayout = findViewById(R.id.register_input_password_layout)
        password = findViewById(R.id.register_input_password)

        confirmPassLayout = findViewById(R.id.register_input_pass_layout)
        confirmPassword = findViewById(R.id.register_input_pass)

        listener()
    }

    private fun listener() {
        btnBack.setOnClickListener(backListener)
        btnRegister.setOnClickListener(registerListener)
    }


    // back activity
    private val backListener = View.OnClickListener {
        finish()
    }

    // listener login
    private val registerListener = View.OnClickListener {
        if (validateForm()) {
            // Disable the button to prevent multiple clicks
            it.isEnabled = false

            // Call createUserWithEmailAndPassword to register the user
            registerEmail()

            Handler(Looper.getMainLooper()).postDelayed({
                if (it.isEnabled) {
                    Toast.makeText(this, "Operation timed out. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }, 10000)
            finish()
        } else {
            return@OnClickListener
        }
    }

    // validate
    private fun validateForm(): Boolean {
        if (!validateEmail(gmail.text.toString())) {
            gmailLayout.helperText = resources.getString(R.string.validateEmail)
            gmailLayout.boxStrokeColor = getColor(R.color.amour)
            return false
        } else {
            gmailLayout.helperText = ""
            gmailLayout.boxStrokeColor = getColor(R.color.joust_blur)
        }
        if (!validatePassword(password.text.toString())) {
            passwordLayout.helperText = resources.getString(R.string.validatePass)
            passwordLayout.boxStrokeColor = getColor(R.color.amour)
            return false
        } else {
            passwordLayout.helperText = ""
            passwordLayout.boxStrokeColor = getColor(R.color.joust_blur)
        }
        if (password.text.toString() != confirmPassword.text.toString()) {
            confirmPassLayout.helperText = resources.getString(R.string.validateConfirmPass)
            confirmPassLayout.boxStrokeColor = getColor(R.color.amour)
            return false
        } else {
            confirmPassLayout.helperText = ""
            confirmPassLayout.boxStrokeColor = getColor(R.color.joust_blur)
        }

        return true
    }

    // register with gmail and password
    private fun registerEmail() {
        val email = gmail.text.toString().trim()
        val password = password.text.toString()
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        btnRegister.isEnabled = true
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        val intent = Intent(this, Main::class.java)
                        startActivity(intent)
                    }
                }
        } catch (e: FirebaseAuthException) {
            Log.e("register exception", e.toString())
        }
    }

    // register with google

}