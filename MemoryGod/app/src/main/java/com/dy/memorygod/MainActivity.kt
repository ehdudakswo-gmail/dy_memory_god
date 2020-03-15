package com.dy.memorygod

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dy.memorygod.adapter.MainRecyclerViewAdapter
import com.dy.memorygod.data.ContactData
import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.manager.ContactManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val dataList: ArrayList<MainData> = ArrayList()
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
                val data = recyclerViewAdapter.selectedItem
                Toast.makeText(this@MainActivity, getInfo(data), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getInfo(data: MainData): String {
        return buildString {
            for (content in data.contentList) {
                appendln(content.title)
                appendln(content.data)
            }
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
                setContactList()
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                Toast.makeText(this@MainActivity, R.string.app_permission_need, Toast.LENGTH_LONG)
                    .show()
                finish()
            }
        }

    private fun setContactList() {
        val contactList = ContactManager.getContactList(this)
        if (contactList == ContactManager.ERROR_CONTACT) {
            Toast.makeText(this, ContactManager.ERROR_MESSAGE, Toast.LENGTH_LONG).show()
            finish()
            return
        }

        setPhoneData(contactList)
        setEmailData(contactList)
        recyclerViewAdapter.refresh(dataList)
    }

    private fun setPhoneData(contactList: List<ContactData>) {
        val subject = getString(R.string.app_main_phone_subject)
        val contentList = ArrayList<MainDataContent>()

        for (contact in contactList) {
            val title = contact.name
            val data = contact.phone
            val content = MainDataContent(title, data)
            contentList.add(content)
        }

        val data = MainData(subject, contentList)
        dataList.add(data)
    }

    private fun setEmailData(contactList: List<ContactData>) {
        val subject = getString(R.string.app_main_email_subject)
        val contentList = ArrayList<MainDataContent>()

        for (contact in contactList) {
            val title = contact.name
            val data = contact.email
            val content = MainDataContent(title, data)
            contentList.add(content)
        }

        val data = MainData(subject, contentList)
        dataList.add(data)
    }

}