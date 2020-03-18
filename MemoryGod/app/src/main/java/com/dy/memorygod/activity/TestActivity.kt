package com.dy.memorygod.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dy.memorygod.R
import com.dy.memorygod.data.MainData
import com.dy.memorygod.enums.IntentName
import com.google.gson.Gson

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val intentName = IntentName.MainData.toString()
        val json = intent.getStringExtra(intentName)
        val data = Gson().fromJson(json, MainData::class.java)

        Toast.makeText(
            this,
            data.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }
}
