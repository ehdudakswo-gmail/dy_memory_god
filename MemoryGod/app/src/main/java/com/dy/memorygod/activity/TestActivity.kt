package com.dy.memorygod.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dy.memorygod.R
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.IntentName
import com.dy.memorygod.manager.ContactManager
import com.dy.memorygod.manager.MainDataManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission

class TestActivity : AppCompatActivity() {

    private val selectedData = MainDataManager.selectedData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val intentName = IntentName.TestConfig.toString()
        val config = intent.getStringExtra(intentName)

        checkDataType()
    }

    private fun checkDataType() {
        if (selectedData.isPhoneData) {
            checkPermissions()
        } else {
            initActivity()
        }
    }

    private fun checkPermissions() {
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setPermissions(
                android.Manifest.permission.READ_CONTACTS
            )
            .check()
    }

    private val permissionListener: PermissionListener =
        object : PermissionListener {
            override fun onPermissionGranted() {
                if (handlePhoneData()) {
                    initActivity()
                }
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                Toast.makeText(
                        this@TestActivity,
                        R.string.app_permission_need, Toast.LENGTH_LONG
                    )
                    .show()
                finish()
            }
        }

    private fun handlePhoneData(): Boolean {
        when (selectedData.subject) {
            getString(R.string.app_main_phoneNumber_subject) -> return setPhoneNumberList()
        }

        return false
    }

    private fun setPhoneNumberList(): Boolean {
        val phoneNumberList = ContactManager.getPhoneNumberList(this)
        if (phoneNumberList == ContactManager.ERROR_CONTACT_PHONE_NUMBER) {
            Toast.makeText(this, ContactManager.ERROR_MESSAGE, Toast.LENGTH_LONG).show()
            finish()
            return false
        }

        val contentList = selectedData.contentList
        contentList.clear()

        for (phoneNumber in phoneNumberList) {
            val title = phoneNumber.name
            val data = phoneNumber.phoneNumber

            val content = MainDataContent(title, data)
            contentList.add(content)
        }

        return true
    }

    private fun initActivity() {
        val subject = selectedData.subject
        val contentList = selectedData.contentList
        val msg = "$subject (${contentList.size})\n$contentList"

        Toast.makeText(
            this,
            msg,
            Toast.LENGTH_SHORT
        ).show()
    }

}