package com.example.splash_screen
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView


class MainActivity : AppCompatActivity() { // splash screen
    lateinit var icon: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        icon = findViewById(R.id.imageView)


        goAnotherScreen()
    }

    private fun goAnotherScreen(){
        icon.alpha = 0f
        icon.animate().setDuration(1000).alpha(1f).withEndAction {
            val intent = Intent(this, Main::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(androidx.appcompat.R.anim.abc_fade_in, androidx.appcompat.R.anim.abc_fade_out)
        }
    }
}