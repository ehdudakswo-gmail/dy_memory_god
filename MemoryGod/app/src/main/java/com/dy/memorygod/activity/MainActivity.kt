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
import com.dy.memorygod.enums.PreferenceKey
import com.dy.memorygod.manager.JsonManager
import com.dy.memorygod.manager.MainDataManager
import com.dy.memorygod.manager.PreferenceManager
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

        val data = MainData(DataType.PhoneNumber, title, contentList, null)
        MainDataManager.dataList.add(data)
    }

    private fun setWordSample() {
        val title = getString(R.string.app_main_wordSample_title)
        val contentList = ArrayList<MainDataContent>()

        contentList.add(MainDataContent("사과", "apple"))
        contentList.add(MainDataContent("한국", "korea"))
        contentList.add(MainDataContent("영어", "english"))

        val data = MainData(DataType.Normal, title, contentList, Date())
        MainDataManager.dataList.add(data)
    }

    private fun loadBackup() {
        val key = PreferenceKey.MainBackupDataList.toString()
        val value = PreferenceManager.get(this, key)

        if (value == PreferenceManager.PREFERENCE_DEFAULT_VALUE) {
            return
        }

        val backupDataList = JsonManager.toMainBackupDataList(value)
        MainDataManager.refreshBackup(backupDataList)
    }

    private fun saveBackup() {
        val dataList = MainDataManager.dataList
        val key = PreferenceKey.MainBackupDataList.toString()
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
                setUpdatedDate(selectedItem)
                MainDataManager.selectedData = selectedItem

                val intent = Intent(this@MainActivity, TestActivity::class.java)
                startActivity(intent)
            }
        })

        refreshRecyclerView()
    }

    private fun setUpdatedDate(data: MainData) {
        when (data.dataType) {
            DataType.Normal -> {
                data.updatedDate = Date()
            }
            else -> {
            }
        }
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
            R.id.main_toolBar_action_item_add -> {
                Toast.makeText(this, "main_toolBar_action_item_add", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}