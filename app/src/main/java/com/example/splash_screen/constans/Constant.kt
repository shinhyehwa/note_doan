package com.example.splash_screen.constans

import android.graphics.Color
import com.example.splash_screen.R

class Constant {
    companion object{
        const val NO_TITLE = "Không có tiêu đề"
        const val HOME_FRAGMENT = "homeFragment"
        const val NAME_DATABASE = "Notes_Database"
        const val TODO_DATABASE = "ToDo_Database"
        const val REQUEST_CODE = 200
        const val CATEGORY = "Category"
        const val NOTE = "Notes"
        const val TODO = "Todos"
        var selectedIndex=R.id.notes

        val colorList = listOf(
            Color.parseColor("#EE5253"),
            Color.parseColor("#0ABDE3"),
            Color.parseColor("#222F3E"),
            Color.parseColor("#FF9F43"),
            Color.parseColor("#576574"),
            Color.parseColor("#F368E0"),
            Color.parseColor("#01A3A4"),
            Color.parseColor("#BE2EDD"),
            Color.parseColor("#F9CA24"),
            Color.parseColor("#FFFFFF"),
            Color.parseColor("#000000")
        )

        val fontList = listOf(
            R.font.aclonica,
            R.font.advent_pro_thin,
            R.font.aguafina_script,
            R.font.alata,
            R.font.alex_brush,
            R.font.alfa_slab_one,
            R.font.alice,
            R.font.andika,
            R.font.annie_use_your_telescope,
            R.font.anton,
            R.font.madimione,
        )

        val colorDefault = Color.parseColor("#000000")
        val fontDefault = R.font.roboto

    }
}