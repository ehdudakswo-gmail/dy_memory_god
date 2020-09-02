package com.dy.memorygod.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.dy.memorygod.R

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        startMainActivity()
    }

    private fun startMainActivity() {
        Handler().postDelayed({
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 500)
    }
}