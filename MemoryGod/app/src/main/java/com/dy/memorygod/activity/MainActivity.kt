package com.dy.memorygod.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dy.memorygod.R
import com.dy.memorygod.adapter.MainRecyclerViewAdapter
import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.DataType
import com.dy.memorygod.enums.DataTypePhone
import com.dy.memorygod.enums.PreferenceKey
import com.dy.memorygod.manager.ContactManager
import com.dy.memorygod.manager.JsonManager
import com.dy.memorygod.manager.MainDataManager
import com.dy.memorygod.manager.PreferenceManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val recyclerViewAdapter = MainRecyclerViewAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MainDataManager.init()
        setDefaultList()
        loadBackup()
        setToolbar()
        setRecyclerView()
    }

    override fun onStart() {
        super.onStart()

        saveBackup()
        refreshRecyclerView()
    }

    private fun setDefaultList() {
        setPhoneNumber()
        setWordSample()
    }

    private fun setPhoneNumber() {
        val title = getString(R.string.app_main_phoneNumber_title)
        val contentList = ArrayList<MainDataContent>()

        val data = MainData(title, contentList, null, DataType.PHONE, DataTypePhone.NUMBER)
        MainDataManager.dataList.add(data)
    }

    private fun setWordSample() {
        val title = getString(R.string.app_main_wordSample_title)
        val contentList = ArrayList<MainDataContent>()

        contentList.add(MainDataContent("Apple", "사과"))
        contentList.add(MainDataContent("Korea", "한국"))
        contentList.add(MainDataContent("English", "영어"))

        val data = MainData(title, contentList, Date(), DataType.NORMAL)
        MainDataManager.dataList.add(data)
    }

    private fun loadBackup() {
        val key = PreferenceKey.MAIN_DATA_LIST.get()
        val value = PreferenceManager.get(this, key)

        if (value == PreferenceManager.PREFERENCE_DEFAULT_VALUE) {
            return
        }

        val backupDataList = JsonManager.toMainBackupDataList(value)
        MainDataManager.refreshBackup(backupDataList)
    }

    private fun saveBackup() {
        val dataList = MainDataManager.dataList
        val key = PreferenceKey.MAIN_DATA_LIST.get()
        val value = JsonManager.toJson(dataList)

        PreferenceManager.set(this, key, value)
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_main)
        val actionBar = supportActionBar!!

        actionBar.setDisplayShowTitleEnabled(false)
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
                MainDataManager.selectedData = selectedItem

                when (selectedItem.dataType) {
                    DataType.NORMAL -> {
                        startTest()
                    }
                    DataType.PHONE -> {
                        checkPermissions()
                    }
                }
            }
        })

        refreshRecyclerView()
    }

    private fun startTest() {
        val selectedData = MainDataManager.selectedData
        selectedData.updatedDate = Date()

        val intent = Intent(this@MainActivity, TestActivity::class.java)
        startActivity(intent)
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
                val selectedData = MainDataManager.selectedData
                val contentList = selectedData.contentList

                when (selectedData.dataType) {
                    DataType.PHONE -> {
                        when (selectedData.dataTypePhone) {
                            DataTypePhone.NUMBER -> {
                                if (setPhoneNumberList(contentList)) {
                                    startTest()
                                }
                            }
                            else -> {
                            }
                        }
                    }
                    else -> {
                    }
                }
            }

            override fun onPermissionDenied(deniedPermissions: java.util.ArrayList<String>?) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.app_permission_request, Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

    private fun setPhoneNumberList(contentList: ArrayList<MainDataContent>): Boolean {
        val phoneNumberList = ContactManager.getPhoneNumberList(this)
        if (phoneNumberList == ContactManager.ERROR_CONTACT_PHONE_NUMBER) {
            Toast.makeText(this, ContactManager.ERROR_MESSAGE, Toast.LENGTH_SHORT).show()
            return false
        }

        contentList.clear()
        for (phoneNumber in phoneNumberList) {
            val title = phoneNumber.name
            val data = phoneNumber.phoneNumber

            val content = MainDataContent(title, data)
            contentList.add(content)
        }

        return true
    }

    private fun refreshRecyclerView() {
        val mainDataList = MainDataManager.dataList
        recyclerViewAdapter.refresh(mainDataList)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_toolBar_menu_add -> {
                Toast.makeText(this, "main_toolBar_action_item_add", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}