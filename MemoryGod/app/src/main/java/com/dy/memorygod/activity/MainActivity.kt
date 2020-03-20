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
import com.dy.memorygod.manager.ContactManager
import com.dy.memorygod.manager.MainDataManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val dataList = MainDataManager.dataList
    private val recyclerViewAdapter = MainRecyclerViewAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setRecyclerView()
        checkPermissions()
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
                setContactList()
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
        recyclerViewAdapter.refresh(dataList)
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

        val data = MainData(subject, contentList)
        dataList.add(data)
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

        val data = MainData(subject, contentList)
        dataList.add(data)
    }

}