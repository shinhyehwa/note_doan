package com.example.splash_screen.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.splash_screen.Main
import com.example.splash_screen.R
import com.example.splash_screen.constans.CustomDialogError
import com.example.splash_screen.constans.validateEmail
import com.example.splash_screen.constans.validatePassword
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider


class Login : AppCompatActivity() {

    private lateinit var gmailLayout: TextInputLayout
    private lateinit var gmail: TextInputEditText

    private lateinit var passwordLayout: TextInputLayout
    private lateinit var password: TextInputEditText

    private lateinit var btnLogin: Button
    private lateinit var btnBack: Button

    private lateinit var btnLoginGoogle: CardView

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        gmail = findViewById(R.id.Login_input_email)
        gmailLayout = findViewById(R.id.login_input_password_layout)
        password = findViewById(R.id.Login_input_password)
        passwordLayout = findViewById(R.id.login_input_password_layout)
        btnLogin = findViewById(R.id.Login_btn)
        btnBack = findViewById(R.id.Login_btn_back)
        btnLoginGoogle = findViewById(R.id.login_google_btn)
        auth = FirebaseAuth.getInstance()
        initial()
    }

    private fun initial() {
        val gos = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignClient = GoogleSignIn.getClient(this,gos)
        listener()
    }

    private fun listener(){
        btnBack.setOnClickListener { finish() }
        btnLogin.setOnClickListener (loginListener)
        btnLoginGoogle.setOnClickListener(listenerLoginWithGoogle)
    }

    private val loginListener = View.OnClickListener {
        if (checkValidate()){
            it.isEnabled = false
            loginWithGmail()
            Handler(Looper.getMainLooper()).postDelayed({
                if (it.isEnabled) {
                    Toast.makeText(this, "Operation timed out. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }, 10000)
        }else{
            return@OnClickListener
        }
    }

    private fun checkValidate(): Boolean{
        if(!validateEmail(gmail.text.toString().trim())){
            gmailLayout.helperText = resources.getString(R.string.validateEmail)
            gmailLayout.boxStrokeColor = getColor(R.color.amour)
            return false
        }else{
            gmailLayout.helperText = ""
            gmailLayout.boxStrokeColor = getColor(R.color.joust_blur)
        }

        if (!validatePassword(password.text.toString().trim())){
            passwordLayout.helperText = resources.getString(R.string.validatePass)
            passwordLayout.boxStrokeColor = getColor(R.color.amour)
            return false
        }else{
            passwordLayout.helperText = ""
            passwordLayout.boxStrokeColor = getColor(R.color.joust_blur)
        }
        return true
    }

    // login with email and password
    private fun loginWithGmail(){
        val email = gmail.text.toString().trim()
        val password = password.text.toString()
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        btnLogin.isEnabled = true
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                        val intent = Intent(this, Main::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        btnLogin.isEnabled = true
                        val dialog = CustomDialogError(this,null, "Tài khoản hoặc mật khẩu không tồn tại")
                        dialog.show()
                    }
                }
        }catch (e: FirebaseAuthException){
            Log.e("login Exception:", e.toString())
        }
    }

    // login with google
    private val listenerLoginWithGoogle = View.OnClickListener {
        val googleSignIntent = googleSignClient.signInIntent
        launcher.launch(googleSignIntent)
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {resul ->
        if(resul.resultCode == Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(resul.data)
            handleResul(task)
        }
    }
    private fun handleResul(task: Task<GoogleSignInAccount>) {
        if(task.isSuccessful){
            val account: GoogleSignInAccount? = task.result
            if (account != null){
                updateUI(account)
            }
        }else{
            Toast.makeText(this,"failed",Toast.LENGTH_LONG).show()
        }
    }
    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                val intent = Intent(this, Main::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}