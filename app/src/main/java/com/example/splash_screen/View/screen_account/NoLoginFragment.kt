package com.example.splash_screen.View.screen_account

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.splash_screen.auth.Login
import com.example.splash_screen.R
import com.example.splash_screen.auth.Register

class NoLoginFragment : Fragment() {

    private lateinit var login: Button
    private  lateinit var register: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_no_login, container, false)
        login = view.findViewById(R.id.NoLogin_login)
        register = view.findViewById(R.id.NoLogin_register)

        initial()
        return view
    }

    private fun initial(){
        login.setOnClickListener{
            val intent = Intent(this.context, Login::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {
            val intent = Intent(this.context, Register::class.java)
            startActivity(intent)
        }
    }
}