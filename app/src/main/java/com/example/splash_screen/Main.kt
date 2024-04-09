package com.example.splash_screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.splash_screen.View.FragmentAccount
import com.example.splash_screen.View.FragmentCalender
import com.example.splash_screen.View.FragmentCheckList
import com.example.splash_screen.View.ScreenNote.FragmentNotes
import com.google.android.material.bottomnavigation.BottomNavigationView

class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val bottomNavi: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        changeScreen(FragmentNotes())
        bottomNavi.setOnItemSelectedListener {
            when(it.itemId){
                R.id.notes -> changeScreen(FragmentNotes())
                R.id.checklist -> changeScreen(FragmentCheckList())
                R.id.calendar -> changeScreen(FragmentCalender())
                R.id.account -> changeScreen(FragmentAccount())
            }
            true
        }

    }


    private fun changeScreen(screen: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.FrameLayout, screen)
        fragmentTransaction.commit()
    }
}