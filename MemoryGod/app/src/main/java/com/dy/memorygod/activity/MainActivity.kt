package com.dy.memorygod.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dy.memorygod.R
import com.dy.memorygod.adapter.MainRecyclerViewAdapter
import com.dy.memorygod.adapter.MainRecyclerViewEventListener
import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.*
import com.dy.memorygod.manager.*
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), MainRecyclerViewEventListener {

    private val recyclerViewAdapter = MainRecyclerViewAdapter(this, this)
    private var mode = ActivityModeMain.NORMAL

    private lateinit var emptyTextView: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MainDataManager.init()
        setDefaultList()
        loadBackup()
        setToolbar()
        setRecyclerView()
        refreshMode(ActivityModeMain.NORMAL)
    }

    override fun onStart() {
        super.onStart()

        refreshContentView(recyclerViewAdapter.dataList)
        saveBackup()
    }

    override fun onDestroy() {
        super.onDestroy()

        saveBackup()
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
        emptyTextView = textView_main_item_empty
        recyclerView = recyclerView_main

        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        recyclerView_main.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> {
                    KeyboardManager.hide(this, recyclerView)
                }
            }

            false
        }

        recyclerViewAdapter.init(recyclerView)
        refreshContentView(MainDataManager.dataList)
    }

    private fun refreshContentView(dataList: MutableList<MainData>) {
        recyclerViewAdapter.refresh(dataList)
        refreshContentViewVisibility(dataList)
    }

    private fun refreshContentViewVisibility(dataList: MutableList<MainData>) {
        when (dataList.count()) {
            0 -> {
                emptyTextView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            }
            else -> {
                emptyTextView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
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

    private fun refreshMode(mode: ActivityModeMain) {
        this.mode = mode

        clearMode()
        setMode()
    }

    private fun clearMode() {
        textView_main_toolbar_title.visibility = View.GONE
        textView_main_toolbar_selection_info.visibility = View.GONE
    }

    private fun setMode() {
        when (mode) {
            ActivityModeMain.NORMAL -> {
                val titleTextView = textView_main_toolbar_title
                titleTextView.visibility = View.VISIBLE
            }
            ActivityModeMain.SELECTION -> {
                val selectionInfoTextView = textView_main_toolbar_selection_info
                val selectionInfoFormat = getString(R.string.app_toolBar_selection_info)
                val selectionSize = recyclerViewAdapter.getSelectionSize()
                val dataSize = recyclerViewAdapter.dataList.size

                selectionInfoTextView.text =
                    String.format(selectionInfoFormat, selectionSize, dataSize)

                selectionInfoTextView.visibility = View.VISIBLE
            }
        }
    }

    override fun onItemClicked(position: Int) {
        if (mode == ActivityModeMain.SELECTION) {
            return
        }

        val data = recyclerViewAdapter.dataList[position]
        MainDataManager.selectedData = data

        when (data.dataType) {
            DataType.NORMAL -> {
                startTest(data, ActivityModeTest.NORMAL)
            }
            DataType.PHONE -> {
                checkPermissions()
            }
        }
    }

    private fun startTest(data: MainData, activityMode: ActivityModeTest) {
        data.updatedDate = Date()
        MainDataManager.selectedData = data

        val intent = Intent(this@MainActivity, TestActivity::class.java)
        val name = IntentName.ACTIVITY_MODE.get()
        intent.putExtra(name, activityMode)
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
                                    startTest(selectedData, ActivityModeTest.NORMAL)
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

    override fun onItemSelected(size: Int) {
        when (size) {
            0 -> {
                refreshMode(ActivityModeMain.NORMAL)
            }
            else -> {
                refreshMode(ActivityModeMain.SELECTION)
            }
        }
    }

    override fun onBackPressed() {
        when (mode) {
            ActivityModeMain.NORMAL -> {
                super.onBackPressed()
            }
            ActivityModeMain.SELECTION -> {
                recyclerViewAdapter.clearSelection()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_toolBar_menu_add -> {
                handleItemAdd()
            }
            R.id.main_toolBar_menu_delete -> {
                when (recyclerViewAdapter.getSelectionSize()) {
                    0 -> {
                        Toast.makeText(
                            this,
                            R.string.app_item_delete_selection_empty,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    else -> {
                        handleItemDelete()
                    }
                }
            }
            R.id.main_toolBar_menu_sort -> {
                if (recyclerViewAdapter.itemCount == 0) {
                    Toast.makeText(
                        this,
                        R.string.app_toolBar_menu_sort_data_empty,
                        Toast.LENGTH_SHORT
                    ).show()
                    return true
                }

                recyclerViewAdapter.clearSelection()
                val intent = Intent(this@MainActivity, MainSortActivity::class.java)
                startActivityForResult(intent, RequestCode.MAIN_SORT.get())
            }
            R.id.main_toolBar_menu_search -> {
                if (recyclerViewAdapter.itemCount == 0) {
                    Toast.makeText(
                        this,
                        R.string.app_toolBar_menu_search_data_empty,
                        Toast.LENGTH_SHORT
                    ).show()
                    return true
                }

                recyclerViewAdapter.clearSelection()
                val intent = Intent(this@MainActivity, MainSearchActivity::class.java)
                startActivityForResult(intent, RequestCode.MAIN_SEARCH.get())
            }
            R.id.main_toolBar_menu_select_all -> {
                recyclerViewAdapter.selectAll()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun handleItemAdd() {
        val itemArr = ItemAddPosition.getDescriptionArr(this)
        val builder = AlertDialog.Builder(this)

        val dialog = builder
            .setTitle(R.string.app_item_add_position_title)
            .setItems(itemArr) { _, which ->
                val title = ""
                val contentList = ArrayList<MainDataContent>()
                val data = MainData(title, contentList, Date(), DataType.NORMAL)

                when (ItemAddPosition.get(which)) {
                    ItemAddPosition.FIRST -> {
                        recyclerViewAdapter.addItemAtFirst(data)
                    }
                    ItemAddPosition.LAST -> {
                        recyclerViewAdapter.addItemAtLast(data)
                    }
                }

                startTest(data, ActivityModeTest.TITLE_EDIT)
            }
            .show()

        dialog.setCanceledOnTouchOutside(false)
    }

    private fun handleItemDelete() {
        val selectionSize = recyclerViewAdapter.getSelectionSize()
        val messageFormat = getString(R.string.app_item_delete_check_message)
        val message = String.format(messageFormat, selectionSize)

        val builder = AlertDialog.Builder(this)
        val dialog = builder
            .setMessage(message)
            .setPositiveButton(R.string.app_dialog_ok, null)
            .setNegativeButton(R.string.app_dialog_cancel, null)
            .show()

        dialog.setCanceledOnTouchOutside(false)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val dataList = recyclerViewAdapter.dataList
            val selectedDataList = recyclerViewAdapter.getSelectedList()

            for (selectedData in selectedDataList) {
                dataList.remove(selectedData)
            }

            refreshView()
            dialog.dismiss()
        }

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun refreshView() {
        refreshContentView(recyclerViewAdapter.dataList)
        recyclerViewAdapter.clearSelection()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RequestCode.MAIN_SORT.get() -> {
                recyclerViewAdapter.notifyDataSetChanged()
                recyclerViewAdapter.scrollToPosition(0)
            }
            RequestCode.MAIN_SEARCH.get() -> {
                val searchData = MainDataManager.searchData ?: return
                recyclerViewAdapter.select(searchData)
            }
        }
    }

}