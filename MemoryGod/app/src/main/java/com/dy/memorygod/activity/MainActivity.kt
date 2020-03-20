package com.dy.memorygod.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dy.memorygod.R
import com.dy.memorygod.adapter.MainRecyclerViewAdapter
import com.dy.memorygod.data.ContactEmailData
import com.dy.memorygod.data.ContactPhoneNumberData
import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.IntentName
import com.dy.memorygod.enums.PreferenceKey
import com.dy.memorygod.manager.ContactManager
import com.dy.memorygod.manager.JsonManager
import com.dy.memorygod.manager.MainDataManager
import com.dy.memorygod.manager.PreferenceManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val recyclerViewAdapter = MainRecyclerViewAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()

        setBackup()
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
                MainDataManager.init()
                setRecyclerView()
                setContactList()
                setTestList()
                refreshBackup()
                refreshRecyclerView()
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                Toast.makeText(
                        this@MainActivity,
                        R.string.app_permission_need, Toast.LENGTH_LONG
                    )
                    .show()
                finish()
            }
        }

    private fun setRecyclerView() {
        val recyclerView = recyclerView_main
        val layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        recyclerViewAdapter.setItemClickListener(object :
            MainRecyclerViewAdapter.ItemClickListener {
            override fun onClick() {
                val selectedItem = recyclerViewAdapter.selectedItem
                if (selectedItem.contentList.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.app_main_contentList_empty,
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                MainDataManager.selectedData = selectedItem
                val intent = Intent(this@MainActivity, TestActivity::class.java)
                val intentName = IntentName.TestConfig.toString()
                val config = selectedItem.subject

                intent.putExtra(intentName, config)
                startActivity(intent)
            }
        })
    }

    private fun setContactList() {
        val phoneNumberList = ContactManager.getPhoneNumberList(this)
        val emailList = ContactManager.getEmailList(this)

        if (phoneNumberList == ContactManager.ERROR_CONTACT_PHONE_NUMBER) {
            Toast.makeText(this, ContactManager.ERROR_MESSAGE, Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setPhoneNumberList(phoneNumberList)
        setEmailList(emailList)
    }

    private fun setPhoneNumberList(phoneNumberList: List<ContactPhoneNumberData>) {
        val subject = getString(R.string.app_main_phoneNumber_subject)
        val contentList = ArrayList<MainDataContent>()

        for (phoneNumber in phoneNumberList) {
            val title = phoneNumber.name
            val data = phoneNumber.phoneNumber

            val content = MainDataContent(title, data)
            contentList.add(content)
        }

        val data = MainData(subject, contentList, true)
        MainDataManager.dataList.add(data)
    }

    private fun setEmailList(emailList: List<ContactEmailData>) {
        val subject = getString(R.string.app_main_email_subject)
        val contentList = ArrayList<MainDataContent>()

        for (email in emailList) {
            val title = email.name
            val data = email.email

            val content = MainDataContent(title, data)
            contentList.add(content)
        }

        val data = MainData(subject, contentList, true)
        MainDataManager.dataList.add(data)
    }

    private fun setTestList() {
        val contentList = ArrayList<MainDataContent>()

        val date1 = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val date2 = SimpleDateFormat("HH:mm:ss").format(Date())
        contentList.add(MainDataContent(date1, date2))

        val data = MainData("SubjectTest", contentList, false)
        MainDataManager.dataList.add(data)
    }

    private fun setBackup() {
        val backupDataList = MainDataManager.getBackupDataList()
        val key = PreferenceKey.MainBackupDataList.toString()
        val value = JsonManager.toJson(backupDataList)

        PreferenceManager.set(this, key, value)

        val msg = buildString { backupDataList.forEach { appendln(it.subject) } }
        Toast.makeText(this, "backupDataList\n$msg", Toast.LENGTH_SHORT).show()
    }

    private fun refreshBackup() {
        val key = PreferenceKey.MainBackupDataList.toString()
        val value = PreferenceManager.get(this, key)

        if (value == PreferenceManager.PREFERENCE_DEFAULT_VALUE) {
            return
        }

        val backupDataList = JsonManager.toMainBackupDataList(value)
        MainDataManager.refreshBackup(backupDataList)
    }

    private fun refreshRecyclerView() {
        val mainDataList = MainDataManager.dataList
        recyclerViewAdapter.refresh(mainDataList)

        val msg = buildString { mainDataList.forEach { appendln(it.subject) } }
        Toast.makeText(this, "mainDataList\n$msg", Toast.LENGTH_SHORT).show()
    }

}