package com.dy.memorygod.activity

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Bundle
import android.os.Handler
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
import com.crashlytics.android.Crashlytics
import com.dy.memorygod.R
import com.dy.memorygod.adapter.MainRecyclerViewAdapter
import com.dy.memorygod.adapter.MainRecyclerViewEventListener
import com.dy.memorygod.data.ContactPhoneNumberData
import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.*
import com.dy.memorygod.manager.*
import com.dy.memorygod.thread.ExcelFileLoadThread
import com.dy.memorygod.thread.ExcelFileSaveThread
import com.dy.memorygod.thread.MainDataLoadThread
import com.dy.memorygod.thread.MainDataSaveThread
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), MainRecyclerViewEventListener {

    private val recyclerViewAdapter = MainRecyclerViewAdapter(this, this)
    private var mode = ActivityModeMain.NORMAL

    private lateinit var emptyTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private val threadDelay = 100L
    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Fabric.with(this, Crashlytics())
        setToolbar()
        setAD()
        setDefaultData()
        loadBackupData()
    }

    override fun onStart() {
        super.onStart()

        if (MainDataManager.isLoadingComplete) {
            refreshContentView(recyclerViewAdapter.dataList)
            saveBackupData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        ProgressDialogManager.hide()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_main)
        val actionBar = supportActionBar!!

        actionBar.setDisplayShowTitleEnabled(false)

        toolbar_main.setOnLongClickListener {
            val info: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            val version = info.versionName

            Toast.makeText(
                this,
                version,
                Toast.LENGTH_SHORT
            ).show()
            true
        }
    }

    private fun setAD() {
        MobileAds.initialize(this) {}

        val adView = adView_main
        val adRequest = AdRequest.Builder().build()

        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(p0: Int) {
                super.onAdFailedToLoad(p0)

                val name = FirebaseAnalyticsEventName.AD_FAILED_TO_LOAD.get()
                val bundle = Bundle()
                bundle.putInt(FirebaseAnalyticsEventParam.INT_VALUE.get(), p0)
                firebaseAnalytics.logEvent(name, bundle)
            }
        }
    }

    private fun setDefaultData() {
        MainDataManager.init()
        setPhoneNumberData()
        setSampleData()
    }

    private fun setPhoneNumberData() {
        val title = getString(R.string.app_phone_number_title)
        val contentList = mutableListOf<MainDataContent>()

        val data = MainData(title, contentList, null, DataType.PHONE, DataTypePhone.NUMBER)
        MainDataManager.dataList.add(data)
    }

    private fun setSampleData() {
        val title = getString(R.string.app_sample_title)
        val contentList = mutableListOf<MainDataContent>()

        contentList.add(
            MainDataContent(
                getString(R.string.app_sample_content1_problem),
                getString(R.string.app_sample_content1_answer),
                TestCheck.NONE
            )
        )
        contentList.add(
            MainDataContent(
                getString(R.string.app_sample_content2_problem),
                getString(R.string.app_sample_content2_answer),
                TestCheck.NONE
            )
        )
        contentList.add(
            MainDataContent(
                getString(R.string.app_sample_content3_problem),
                getString(R.string.app_sample_content3_answer),
                TestCheck.NONE
            )
        )

        val data = MainData(title, contentList, Date(), DataType.NORMAL, DataTypePhone.NONE)
        MainDataManager.dataList.add(data)
    }

    private fun loadBackupData() {
        val message = getString(R.string.app_backup_load_message)
        ProgressDialogManager.show(this, message)

        Handler().postDelayed({
            val thread = MainDataLoadThread(this)
            thread.start()
            thread.join()

            val exception = thread.exception
            if (exception != null) {
                val errorFormat = getString(R.string.app_backup_load_error)
                val errorMessage = String.format(errorFormat, exception)
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()

                val name = FirebaseAnalyticsEventName.MAIN_DATA_DB_LOAD_ERROR.get()
                val bundle = Bundle()
                bundle.putString(FirebaseAnalyticsEventParam.MESSAGE.get(), errorMessage)
                firebaseAnalytics.logEvent(name, bundle)
            }

            setRecyclerView()
            refreshMode(ActivityModeMain.NORMAL)
            MainDataManager.setLoadingComplete()
            ProgressDialogManager.hide()
        }, threadDelay)
    }

    private fun saveBackupData() {
        val thread = MainDataSaveThread(this)
        thread.start()
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
                when (data.dataTypePhone) {
                    DataTypePhone.NUMBER -> {
                        checkPhoneNumberPermission()
                    }
                    else -> {
                    }
                }
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

    private fun checkPhoneNumberPermission() {
        TedPermission.with(this)
            .setPermissionListener(phoneNumberPermissionListener)
            .setPermissions(
                android.Manifest.permission.READ_CONTACTS
            )
            .check()
    }

    private val phoneNumberPermissionListener: PermissionListener =
        object : PermissionListener {
            override fun onPermissionGranted() {
                val selectedData = MainDataManager.selectedData
                clearPhoneNumber(selectedData)

                val phoneNumberList = ContactManager.getPhoneNumberList(this@MainActivity)
                if (phoneNumberList == ContactManager.ERROR_CONTACT_PHONE_NUMBER) {
                    val errorMessage = ContactManager.ERROR_MESSAGE
                    AlertDialogManager.show(this@MainActivity, errorMessage)
                    refreshContentView(MainDataManager.dataList)

                    val name = FirebaseAnalyticsEventName.PHONE_NUMBER_LOAD_ERROR.get()
                    val bundle = Bundle()
                    bundle.putString(FirebaseAnalyticsEventParam.MESSAGE.get(), errorMessage)
                    firebaseAnalytics.logEvent(name, bundle)
                    return
                }

                setPhoneNumber(selectedData, phoneNumberList)
                startTest(selectedData, ActivityModeTest.NORMAL)
            }

            override fun onPermissionDenied(deniedPermissions: java.util.ArrayList<String>?) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.app_permission_request, Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

    private fun clearPhoneNumber(data: MainData) {
        val contentList = data.contentList
        contentList.clear()
    }

    private fun setPhoneNumber(data: MainData, phoneNumberList: List<ContactPhoneNumberData>) {
        val contentList = data.contentList

        for (phoneNumber in phoneNumberList) {
            val problem = phoneNumber.name
            val answer = phoneNumber.phoneNumber

            val content = MainDataContent(problem, answer, TestCheck.NONE)
            contentList.add(content)
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
                finishWithBackup()
            }
            ActivityModeMain.SELECTION -> {
                recyclerViewAdapter.clearSelection()
            }
        }
    }

    private fun finishWithBackup() {
        val message = getString(R.string.app_backup_save_message)
        ProgressDialogManager.show(this, message)

        Handler().postDelayed({
            val thread = MainDataSaveThread(this)
            thread.start()
            thread.join()

            val exception = thread.exception
            if (exception != null) {
                val errorFormat = getString(R.string.app_backup_save_error)
                val errorMessage = String.format(errorFormat, exception)
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()

                val name = FirebaseAnalyticsEventName.MAIN_DATA_DB_SAVE_ERROR.get()
                val bundle = Bundle()
                bundle.putString(FirebaseAnalyticsEventParam.MESSAGE.get(), errorMessage)
                firebaseAnalytics.logEvent(name, bundle)
            }

            super.onBackPressed()
        }, threadDelay)
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
            R.id.main_toolBar_menu_copy -> {
                handleItemCopy()
            }
            R.id.main_toolBar_menu_delete -> {
                when (recyclerViewAdapter.getSelectionSize()) {
                    0 -> {
                        Toast.makeText(
                            this,
                            R.string.app_item_delete_selection_empty,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        val selectedDataList = recyclerViewAdapter.getSelectedList()
                        val phoneData = selectedDataList.find { it.dataType == DataType.PHONE }

                        if (phoneData != null) {
                            val format =
                                getString(R.string.app_item_delete_selection_not_allowed)
                            val title = phoneData.title
                            val message = String.format(format, title)
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                            return true
                        }

                        handleItemDelete(selectedDataList)
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
            R.id.main_toolBar_menu_file_save -> {
                checkFileSavePermission()
            }
            R.id.main_toolBar_menu_file_load -> {
                checkFileLoadPermission()
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
                val data =
                    MainData(title, contentList, Date(), DataType.NORMAL, DataTypePhone.NONE)

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

    private fun handleItemCopy() {
        when (recyclerViewAdapter.getSelectionSize()) {
            0 -> {
                Toast.makeText(
                    this,
                    R.string.app_item_copy_selection_empty,
                    Toast.LENGTH_SHORT
                ).show()
            }
            1 -> {
                val dataList = recyclerViewAdapter.dataList
                val selectedDataList = recyclerViewAdapter.getSelectedList()
                val selectedData = selectedDataList[0]
                val selectedIdx = dataList.indexOf(selectedData)

                if (selectedIdx == -1) {
                    Toast.makeText(this, R.string.app_item_copy_not_found, Toast.LENGTH_SHORT)
                        .show()
                    return
                }

                refreshPhoneData(dataList)

                val copyIdx = selectedIdx + 1
                val copyData = ItemCopyManager.create(this, selectedData)

                dataList.add(copyIdx, copyData)
                recyclerViewAdapter.clearSelection()
                recyclerViewAdapter.select(copyData)
                refreshContentView(dataList)
            }
            else -> {
                Toast.makeText(
                    this,
                    R.string.app_item_copy_selection_one_request,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleItemDelete(selectedDataList: List<MainData>) {
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

    private fun checkFileSavePermission() {
        TedPermission.with(this)
            .setPermissionListener(fileSavePermissionListener)
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .check()
    }

    private val fileSavePermissionListener: PermissionListener =
        object : PermissionListener {
            override fun onPermissionGranted() {
                handleFileSave()
            }

            override fun onPermissionDenied(deniedPermissions: java.util.ArrayList<String>?) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.app_permission_request, Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

    private fun handleFileSave() {
        val dataList = MainDataManager.dataList
        if (dataList.isEmpty()) {
            Toast.makeText(
                this,
                R.string.app_file_save_data_none,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val hashSet = HashSet<String>()
        for (data in dataList) {
            val title = data.title
            if (hashSet.contains(title)) {
                val errorFormat = getString(R.string.app_toolBar_menu_file_save_error_title_overlap)
                val errorMessage = String.format(errorFormat, title)
                AlertDialogManager.show(this, errorMessage)
                return
            }

            hashSet.add(title)
        }

        val message = getString(R.string.app_toolBar_menu_file_save_progress_message)
        ProgressDialogManager.show(this, message)

        refreshPhoneData(dataList)

        val appName = getString(R.string.app_name)
        val date = DateManager.getExcelFileName()

        val fileExtension = ExcelManager.fileExtension
        val fileNameFormat = getString(R.string.app_file_name_format)
        val fileName = String.format(fileNameFormat, appName, date, fileExtension)

        Handler().postDelayed({
            val filePath = ExcelManager.filePath
            val thread = ExcelFileSaveThread(dataList, filePath, fileName)
            thread.start()
            thread.join()

            val exception = thread.exception
            if (exception != null) {
                val errorFormat = getString(R.string.app_toolBar_menu_file_save_error)
                val errorMessage = String.format(errorFormat, exception)

                AlertDialogManager.show(this, errorMessage)
                ProgressDialogManager.hide()

                val name = FirebaseAnalyticsEventName.MAIN_DATA_EXCEL_SAVE_ERROR.get()
                val bundle = Bundle()
                bundle.putString(FirebaseAnalyticsEventParam.MESSAGE.get(), errorMessage)
                firebaseAnalytics.logEvent(name, bundle)
                return@postDelayed
            }

            val file = thread.file
            val isFileNone = !(file.isFile && file.exists())

            if (isFileNone) {
                val fileNoneFormat = getString(R.string.app_toolBar_menu_file_save_error)
                val fileNoneDescription =
                    getString(R.string.app_toolBar_menu_file_save_error_file_none)
                val fileNoneMessage = String.format(fileNoneFormat, fileNoneDescription)

                Toast.makeText(this, fileNoneMessage, Toast.LENGTH_SHORT).show()
                ProgressDialogManager.hide()
                return@postDelayed
            }

            val downloadManager =
                getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            downloadManager.addCompletedDownload(
                file.name,
                file.name,
                false,
                ExcelManager.fileMimeType,
                file.absolutePath,
                file.length(),
                true
            )

            val filePathType = ExcelManager.filePathType
            val directoryFormat = getString(R.string.app_directory)
            val directory = String.format(directoryFormat, filePathType)

            val completeMessageFormat =
                getString(R.string.app_toolBar_menu_file_save_complete)
            val completeMessage = String.format(completeMessageFormat, directory)

            Toast.makeText(
                this,
                completeMessage,
                Toast.LENGTH_SHORT
            ).show()

            ProgressDialogManager.hide()

        }, threadDelay)
    }

    private fun refreshPhoneData(dataList: MutableList<MainData>) {
        for (data in dataList) {
            if (data.dataType == DataType.PHONE && data.dataTypePhone == DataTypePhone.NUMBER) {
                refreshPhoneNumber(data)
            }
        }
    }

    private fun refreshPhoneNumber(data: MainData) {
        if (data.contentList.isEmpty()) {
            return
        }

        clearPhoneNumber(data)
        val phoneNumberList = ContactManager.getPhoneNumberList(this@MainActivity)

        if (phoneNumberList == ContactManager.ERROR_CONTACT_PHONE_NUMBER) {
            val errorMessage = ContactManager.ERROR_MESSAGE
            Toast.makeText(
                this@MainActivity,
                errorMessage,
                Toast.LENGTH_SHORT
            ).show()

            val name = FirebaseAnalyticsEventName.PHONE_NUMBER_REFRESH_ERROR.get()
            val bundle = Bundle()
            bundle.putString(FirebaseAnalyticsEventParam.MESSAGE.get(), errorMessage)
            firebaseAnalytics.logEvent(name, bundle)
            return
        }

        setPhoneNumber(data, phoneNumberList)
    }

    private fun checkFileLoadPermission() {
        TedPermission.with(this)
            .setPermissionListener(fileLoadPermissionListener)
            .setPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .check()
    }

    private val fileLoadPermissionListener: PermissionListener =
        object : PermissionListener {
            override fun onPermissionGranted() {
                handleFileLoad()
            }

            override fun onPermissionDenied(deniedPermissions: java.util.ArrayList<String>?) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.app_permission_request, Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

    private fun handleFileLoad() {
        val filePathType = ExcelManager.filePathType
        val directoryFormat = getString(R.string.app_directory)
        val directory = String.format(directoryFormat, filePathType)

        val excelFileList = ExcelManager.getFileList()
        val itemArr = excelFileList.map { it.name }.toTypedArray()

        if (itemArr.isEmpty()) {
            val fileExtension = ExcelManager.fileExtension
            val messageFormat = getString(R.string.app_file_load_data_none)
            val message = String.format(messageFormat, fileExtension, directory)

            Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val builder = AlertDialog.Builder(this)
        val dialog = builder
            .setTitle(directory)
            .setItems(itemArr) { _, which ->
                val message = getString(R.string.app_toolBar_menu_file_load_progress_message)
                ProgressDialogManager.show(this, message)

                Handler().postDelayed({
                    val file = excelFileList[which]
                    val thread = ExcelFileLoadThread(file)
                    thread.start()
                    thread.join()

                    val exception = thread.exception
                    if (exception != null) {
                        val errorFormat = getString(R.string.app_toolBar_menu_file_load_error)
                        val errorMessage = String.format(errorFormat, exception)

                        AlertDialogManager.show(this, errorMessage)
                        ProgressDialogManager.hide()

                        val name = FirebaseAnalyticsEventName.MAIN_DATA_EXCEL_LOAD_ERROR.get()
                        val bundle = Bundle()
                        bundle.putString(FirebaseAnalyticsEventParam.MESSAGE.get(), errorMessage)
                        firebaseAnalytics.logEvent(name, bundle)
                        return@postDelayed
                    }

                    val dataList = thread.dataList
                    handleFileLoadData(dataList)
                    ProgressDialogManager.hide()
                }, threadDelay)
            }
            .show()

        dialog.setCanceledOnTouchOutside(false)
    }

    private fun handleFileLoadData(dataList: List<MainData>) {
        val itemArr = dataList.map { it.title }.toTypedArray()
        val checkArr = dataList.map { false }.toBooleanArray()

        val builder = AlertDialog.Builder(this)
        val dialog = builder
            .setTitle(R.string.app_file_load_data_title)
            .setPositiveButton(R.string.app_dialog_ok, null)
            .setNegativeButton(R.string.app_dialog_cancel, null)
            .setNeutralButton(R.string.app_dialog_all_selection_flag, null)
            .setMultiChoiceItems(itemArr, checkArr, null)
            .show()

        dialog.setCanceledOnTouchOutside(false)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val checkedDataList = ArrayList<MainData>()
            val listView = dialog.listView

            for (i in dataList.indices) {
                if (listView.isItemChecked(i)) {
                    val data = dataList[i]
                    checkedDataList.add(data)
                }
            }

            if (checkedDataList.isEmpty()) {
                Toast.makeText(
                    this,
                    R.string.app_toolBar_menu_file_load_data_check_none,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            dialog.dismiss()
            handleFileLoadDataPosition(checkedDataList)
        }

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            dialog.dismiss()
        }
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
            val listView = dialog.listView
            val checkedCount = listView.checkedItemCount
            val isAllCheck = (checkedCount == dataList.size)
            val setCheck = !isAllCheck

            for (i in dataList.indices) {
                listView.setItemChecked(i, setCheck)
            }
        }
    }

    private fun handleFileLoadDataPosition(checkedDataList: List<MainData>) {
        val itemArr = ItemAddPosition.getDescriptionArr(this)
        val dataList = MainDataManager.dataList

        val builder = AlertDialog.Builder(this)
        val dialog = builder
            .setTitle(R.string.app_item_add_position_title)
            .setItems(itemArr) { _, which ->
                when (ItemAddPosition.get(which)) {
                    ItemAddPosition.FIRST -> {
                        for (i in checkedDataList.lastIndex downTo 0) {
                            val checkedData = checkedDataList[i]
                            dataList.add(0, checkedData)
                        }
                    }
                    ItemAddPosition.LAST -> {
                        for (checkedData in checkedDataList) {
                            dataList.add(checkedData)
                        }
                    }
                }

                refreshView()
            }
            .show()

        dialog.setCanceledOnTouchOutside(false)
    }

}