package com.dy.memorygod.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dy.memorygod.R
import com.dy.memorygod.enums.IntentName
import com.dy.memorygod.manager.MainDataManager

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val intentName = IntentName.TestConfig.toString()
        val config = intent.getStringExtra(intentName)

        val selectedData = MainDataManager.selectedData
        if (selectedData.isPhoneData) {
            Toast.makeText(
                this,
                selectedData.toString(),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this,
                "Writable",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}