package com.dy.memorygod

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dy.memorygod.data.ContactData
import com.dy.memorygod.manager.ContactManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()
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

        textView_main.text = getContactListInfo(contactList)
    }

    private fun getContactListInfo(contactList: List<ContactData>): String {
        return buildString {
            for (contact in contactList) {
                val info = "${contact.name} : ${contact.phone}"
                appendln(info)
            }
        }
    }

}