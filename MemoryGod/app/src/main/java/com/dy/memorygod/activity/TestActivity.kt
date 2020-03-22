package com.dy.memorygod.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dy.memorygod.R
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.IntentName
import com.dy.memorygod.manager.ContactManager
import com.dy.memorygod.manager.MainDataManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_test.*

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
//        val subject = selectedData.subject
//        val contentList = selectedData.contentList
//        val msg = "$subject (${contentList.size})\n$contentList"
//
//        Toast.makeText(
//            this,
//            msg,
//            Toast.LENGTH_SHORT
//        ).show()

        setToolbar()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_test)
        val actionBar = supportActionBar!!

        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)
        textView_toolbar_test_title.text = selectedData.subject
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_test, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.test_toolBar_action_item_add -> {
                Toast.makeText(this, "test_toolBar_action_item_add", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}