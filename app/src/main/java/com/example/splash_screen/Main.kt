package com.example.splash_screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.splash_screen.View.calander.FragmentCalender
import com.example.splash_screen.View.FragmentCheckList
import com.example.splash_screen.View.ScreenNote.FragmentNotes
import com.example.splash_screen.View.screen_account.FragmentAccount
import com.example.splash_screen.View.screen_account.NoLoginFragment
import com.example.splash_screen.constans.const
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class Main : AppCompatActivity() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val bottomNavi: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        changeScreen(FragmentNotes())
        val currentUser = firebaseAuth.currentUser
        currentUser?.reload()
        bottomNavi.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.notes -> {
                    changeScreen(FragmentNotes())
                    const.selectedIndex= R.id.notes
                }
                R.id.checklist -> changeScreen(FragmentCheckList())
                R.id.calendar -> changeScreen(FragmentCalender())
                R.id.account -> {
                    if (currentUser != null) {
                        changeScreen(FragmentAccount())
                    } else {
                        changeScreen(NoLoginFragment())
                    }
                    const.selectedIndex= R.id.account
                }
            }
            true
        }
        bottomNavi.selectedItemId= const.selectedIndex

    }


    private fun changeScreen(screen: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.FrameLayout, screen)
        fragmentTransaction.commit()
    }
}