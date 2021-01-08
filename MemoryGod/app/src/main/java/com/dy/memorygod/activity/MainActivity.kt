package com.dy.memorygod.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
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
import com.dy.memorygod.GlobalApplication
import com.dy.memorygod.R
import com.dy.memorygod.adapter.MainRecyclerViewAdapter
import com.dy.memorygod.adapter.MainRecyclerViewEventListener
import com.dy.memorygod.data.ContactPhoneNumberData
import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.entity.MainDataEntity
import com.dy.memorygod.enums.*
import com.dy.memorygod.manager.*
import com.dy.memorygod.thread.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.firestore.FirebaseFirestore
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
    private val threadDelay = 100L

    // Firebase
    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    private val firestoreDB = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar()
        setFirestoreConfig()
        setAD()
        setData()
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

        ProgressDialogManager.hide(this)
        AlertDialogManager.hide(this)
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAlertDialog(message: String) {
        runOnUiThread {
            AlertDialogManager.show(this, message)
        }
    }

    private fun showProgressDialog(message: String) {
        runOnUiThread {
            ProgressDialogManager.show(this, message)
        }
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_main)
        val actionBar = supportActionBar!!

        actionBar.setDisplayShowTitleEnabled(false)

        textView_main_toolbar_title.setOnLongClickListener {
            val appInfo = GlobalApplication.instance.getAppInfo()
            showToast(appInfo)

            true
        }
    }

    private fun setFirestoreConfig() {
        firestoreDB.collection(FirestoreManager.COLLECTION_CONFIG)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.id == FirestoreManager.COLLECTION_CONFIG_DOCUMENT_FIRESTORE) {
                        val isAllEnableKey =
                            FirestoreManager.COLLECTION_CONFIG_DOCUMENT_FIRESTORE_FIELD_isAllEnable
                        val isLogEnableKey =
                            FirestoreManager.COLLECTION_CONFIG_DOCUMENT_FIRESTORE_FIELD_isLogEnable

                        val data = document.data
                        val isAllEnable = data[isAllEnableKey] as Boolean
                        val isLogEnable = data[isLogEnableKey] as Boolean

                        val appConfig = GlobalApplication.instance.firestoreConfig
                        appConfig.isAllEnable = isAllEnable
                        appConfig.isLogEnable = isLogEnable

                        LogsManager.d("setFirestoreConfig addOnSuccessListener data : $data")
                    }
                }
            }
            .addOnFailureListener { exception ->
                val errorMessage = exception.toString()
                showToast(errorMessage)

                LogsManager.d("setFirestoreConfig addOnFailureListener errorMessage : $errorMessage")
            }
    }

    private fun setAD() {
        MobileAds.initialize(this) {}

        val adView = adView_main
        val adRequest = AdRequest.Builder().build()

        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                super.onAdFailedToLoad(errorCode)

                val name = FirebaseAnalyticsEventName.AD_FAILED_TO_LOAD.get()
                val nameWithValue = "${name}_${errorCode}"
                val bundle = Bundle()
                bundle.putInt(FirebaseAnalyticsEventParam.INT_VALUE.get(), errorCode)
                firebaseAnalytics.logEvent(nameWithValue, bundle)
            }
        }
    }

    private fun setData() {
        val message = getString(R.string.app_data_load_progress_message)
        showProgressDialog(message)
        MainDataManager.init()

        val thread = BackupDataLoadThread(this)
        thread.start()
        thread.join()

        val backupData = thread.backupData
//        LogsManager.d("setData backupData : $backupData")

        if (backupData == null || backupData.isEmpty()) {
            setPhoneNumberData()
            loadSampleData()
        } else {
            loadMainData(backupData)
        }

        val exception = thread.exception
        LogsManager.d("setData backupData exception: $exception")

        if (exception != null) {
            // Message
            val errorMessage = FirestoreLogType.BACKUP_DATA_LOAD_ERROR.get()
            showToast(errorMessage)

            // Firestore Log
            if (FirestoreManager.isLogEnable()) {
                firestoreDB.collection(FirestoreManager.COLLECTION_LOGS)
                    .document(FirestoreManager.COLLECTION_LOGS_DOCUMENT_DATE)
                    .collection(FirestoreManager.getLogsDate())
                    .add(
                        FirestoreManager.getLogData(
                            this,
                            FirestoreLogType.BACKUP_DATA_LOAD_ERROR,
                            exception
                        )
                    )
                    .addOnSuccessListener { documentReference ->
                        LogsManager.d("BACKUP_DATA_LOAD_ERROR addOnSuccessListener documentReference.id : ${documentReference.id}")
                    }
                    .addOnFailureListener { error ->
                        LogsManager.d("BACKUP_DATA_LOAD_ERROR addOnFailureListener error : $error")
                    }
            }

            // FirebaseCrashlytics Log
            FirebaseCrashlyticsManager.record(
                FirebaseCrashlyticsLogType.BACKUP_DATA_LOAD_ERROR,
                exception
            )
        }
    }

    private fun setPhoneNumberData() {
        val title = getString(R.string.app_phone_number_title)
        val contentList = mutableListOf<MainDataContent>()

        val data = MainData(title, contentList, null, DataType.PHONE, DataTypePhone.NUMBER)
        MainDataManager.dataList.add(data)
    }

    private fun loadSampleData() {
        Handler().postDelayed({
            val thread = SampleDataLoadThread(this)
            thread.start()
            thread.join()
            init()

            val exception = thread.exception
            LogsManager.d("setSampleData exception : $exception")

            if (exception != null) {
                // Message
                val errorMessage = FirestoreLogType.SAMPLE_DATA_LOAD_ERROR.get()
                showToast(errorMessage)

                // Firestore Log
                if (FirestoreManager.isLogEnable()) {
                    firestoreDB.collection(FirestoreManager.COLLECTION_LOGS)
                        .document(FirestoreManager.COLLECTION_LOGS_DOCUMENT_DATE)
                        .collection(FirestoreManager.getLogsDate())
                        .add(
                            FirestoreManager.getLogData(
                                this,
                                FirestoreLogType.SAMPLE_DATA_LOAD_ERROR,
                                exception
                            )
                        )
                        .addOnSuccessListener { documentReference ->
                            LogsManager.d("SAMPLE_DATA_LOAD_ERROR addOnSuccessListener documentReference.id : ${documentReference.id}")
                        }
                        .addOnFailureListener { error ->
                            LogsManager.d("SAMPLE_DATA_LOAD_ERROR addOnFailureListener error : $error")
                        }
                }

                // FirebaseCrashlytics Log
                FirebaseCrashlyticsManager.record(
                    FirebaseCrashlyticsLogType.SAMPLE_DATA_LOAD_ERROR,
                    exception
                )
            }
        }, threadDelay)
    }

    private fun loadMainData(backupData: List<MainDataEntity>) {
        Handler().postDelayed({
            val thread = MainDataLoadThread(backupData)
            thread.start()
            thread.join()
            init()

            val exception = thread.exception
            if (exception != null) {
                // Message
                val errorMessage = FirestoreLogType.MAIN_DATA_DB_LOAD_ERROR.get()
                showToast(errorMessage)

                // FirebaseAnalytics Log
                val name = FirebaseAnalyticsEventName.MAIN_DATA_DB_LOAD_ERROR.get()
                val bundle = Bundle()
                bundle.putString(FirebaseAnalyticsEventParam.MESSAGE.get(), errorMessage)
                firebaseAnalytics.logEvent(name, bundle)

                // Firestore Log
                if (FirestoreManager.isLogEnable()) {
                    firestoreDB.collection(FirestoreManager.COLLECTION_LOGS)
                        .document(FirestoreManager.COLLECTION_LOGS_DOCUMENT_DATE)
                        .collection(FirestoreManager.getLogsDate())
                        .add(
                            FirestoreManager.getLogData(
                                this,
                                FirestoreLogType.MAIN_DATA_DB_LOAD_ERROR,
                                exception
                            )
                        )
                        .addOnSuccessListener { documentReference ->
                            LogsManager.d("MAIN_DATA_DB_LOAD_ERROR addOnSuccessListener documentReference.id : ${documentReference.id}")
                        }
                        .addOnFailureListener { error ->
                            LogsManager.d("MAIN_DATA_DB_LOAD_ERROR addOnFailureListener error : $error")
                        }
                }

                // FirebaseCrashlytics Log
                FirebaseCrashlyticsManager.record(
                    FirebaseCrashlyticsLogType.MAIN_DATA_DB_LOAD_ERROR,
                    exception
                )
            }
        }, threadDelay)
    }

    private fun init() {
        setRecyclerView()
        refreshMode(ActivityModeMain.NORMAL)
        MainDataManager.setLoadingComplete()
        ProgressDialogManager.hide(this)
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

        // FirebaseAnalytics Log
        val name = FirebaseAnalyticsEventName.MAIN_ITEM_CLICK.get()
        val bundle = Bundle()
        bundle.putString(FirebaseAnalyticsEventParam.MESSAGE.get(), data.title)
        firebaseAnalytics.logEvent(name, bundle)

        // Firestore Log
        if (FirestoreManager.isLogEnable()) {
            firestoreDB.collection(FirestoreManager.COLLECTION_LOGS)
                .document(FirestoreManager.COLLECTION_LOGS_DOCUMENT_DATE)
                .collection(FirestoreManager.getLogsDate())
                .add(
                    FirestoreManager.getLogData(
                        this,
                        FirestoreLogType.MAIN_ITEM_CLICK,
                        data.title
                    )
                )
                .addOnSuccessListener { documentReference ->
                    LogsManager.d("MAIN_ITEM_CLICK addOnSuccessListener documentReference.id : ${documentReference.id}")
                }
                .addOnFailureListener { error ->
                    LogsManager.d("MAIN_ITEM_CLICK addOnFailureListener error : $error")
                }
        }

        // FirebaseCrashlytics Log
        FirebaseCrashlyticsManager.record(FirebaseCrashlyticsLogType.MAIN_ITEM_CLICK, data.title)
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
                    showAlertDialog(errorMessage)
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
        showProgressDialog(message)

        Handler().postDelayed({
            val thread = MainDataSaveThread(this)
            thread.start()
            thread.join()

            val exception = thread.exception
            if (exception != null) {
                // Message
                val errorMessage = FirestoreLogType.MAIN_DATA_DB_SAVE_ERROR.get()
                showToast(errorMessage)

                // FirebaseAnalytics Log
                val name = FirebaseAnalyticsEventName.MAIN_DATA_DB_SAVE_ERROR.get()
                val bundle = Bundle()
                bundle.putString(FirebaseAnalyticsEventParam.MESSAGE.get(), errorMessage)
                firebaseAnalytics.logEvent(name, bundle)

                // Firestore Log
                if (FirestoreManager.isLogEnable()) {
                    firestoreDB.collection(FirestoreManager.COLLECTION_LOGS)
                        .document(FirestoreManager.COLLECTION_LOGS_DOCUMENT_DATE)
                        .collection(FirestoreManager.getLogsDate())
                        .add(
                            FirestoreManager.getLogData(
                                this,
                                FirestoreLogType.MAIN_DATA_DB_SAVE_ERROR,
                                exception
                            )
                        )
                        .addOnSuccessListener { documentReference ->
                            LogsManager.d("MAIN_DATA_DB_SAVE_ERROR addOnSuccessListener documentReference.id : ${documentReference.id}")
                        }
                        .addOnFailureListener { error ->
                            LogsManager.d("MAIN_DATA_DB_SAVE_ERROR addOnFailureListener error : $error")
                        }
                }

                // FirebaseCrashlytics Log
                FirebaseCrashlyticsManager.record(
                    FirebaseCrashlyticsLogType.MAIN_DATA_DB_SAVE_ERROR,
                    exception
                )
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
                handleItemDelete()
            }
            R.id.main_toolBar_menu_sort -> {
                handleItemSort()
            }
            R.id.main_toolBar_menu_search -> {
                handleItemSearch()
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

    private fun handleItemDelete() {
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
                    return
                }

                handleItemDelete(selectedDataList)
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

    private fun handleItemSort() {
        if (recyclerViewAdapter.itemCount == 0) {
            Toast.makeText(
                this,
                R.string.app_toolBar_menu_sort_data_empty,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        recyclerViewAdapter.clearSelection()
        val intent = Intent(this@MainActivity, MainSortActivity::class.java)
        startActivityForResult(intent, RequestCode.MAIN_SORT.get())
    }

    private fun handleItemSearch() {
        if (recyclerViewAdapter.itemCount == 0) {
            Toast.makeText(
                this,
                R.string.app_toolBar_menu_search_data_empty,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        recyclerViewAdapter.clearSelection()
        val intent = Intent(this@MainActivity, MainSearchActivity::class.java)
        startActivityForResult(intent, RequestCode.MAIN_SEARCH.get())
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

    private fun refreshView() {
        refreshContentView(recyclerViewAdapter.dataList)
        recyclerViewAdapter.clearSelection()
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
                checkDataState()
            }

            override fun onPermissionDenied(deniedPermissions: java.util.ArrayList<String>?) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.app_permission_request, Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

    private fun checkDataState() {
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
                showAlertDialog(errorMessage)
                return
            }

            hashSet.add(title)
        }

        showFileSaveBrowser()
    }

    private fun showFileSaveBrowser() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = FileMimeType.EXCEL.value
            putExtra(Intent.EXTRA_TITLE, getExcelFileName())
        }

        startActivityForResult(intent, RequestCode.MAIN_FILE_SAVE.get())
    }

    private fun getExcelFileName(): String {
        val appName = getString(R.string.app_name)
        val date = DateManager.getExcelFileName()
        val fileExtension = ExcelManager.fileExtension
        val fileNameFormat = getString(R.string.app_file_name_format)

        return String.format(fileNameFormat, appName, date, fileExtension)
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
                showFileOpenBrowser()
            }

            override fun onPermissionDenied(deniedPermissions: java.util.ArrayList<String>?) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.app_permission_request, Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

    fun showFileOpenBrowser() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = FileMimeType.EXCEL.value
        }

        startActivityForResult(intent, RequestCode.MAIN_FILE_OPEN.get())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)

        when (requestCode) {
            RequestCode.MAIN_SORT.get() -> {
                recyclerViewAdapter.notifyDataSetChanged()
                recyclerViewAdapter.scrollToPosition(0)
            }
            RequestCode.MAIN_SEARCH.get() -> {
                val searchData = MainDataManager.searchData ?: return
                recyclerViewAdapter.select(searchData)
            }
            RequestCode.MAIN_FILE_SAVE.get() -> {
                if (resultCode == Activity.RESULT_OK) {
                    resultData?.data?.also { uri ->
                        handleFileSave(uri)
                    }
                }
            }
            RequestCode.MAIN_FILE_OPEN.get() -> {
                if (resultCode == Activity.RESULT_OK) {
                    resultData?.data?.also { uri ->
                        handleFileLoad(uri)
                    }
                }
            }
        }
    }

    private fun handleFileSave(uri: Uri) {
        val loadingMessage = getString(R.string.app_toolBar_menu_file_save_progress_message)
        showProgressDialog(loadingMessage)

        val dataList = MainDataManager.dataList
        refreshPhoneData(dataList)

        Handler().postDelayed({
            val thread = ExcelFileSaveThread(this, uri, dataList)
            thread.start()
            thread.join()

            val exception = thread.exception
            if (exception != null) {
                val errorFormat = getString(R.string.app_toolBar_menu_file_save_error)
                val errorMessage = String.format(errorFormat, exception)

                showAlertDialog(errorMessage)
                ProgressDialogManager.hide(this)

                // FirebaseAnalytics Log
                val name = FirebaseAnalyticsEventName.MAIN_DATA_EXCEL_SAVE_ERROR.get()
                val bundle = Bundle()
                bundle.putString(FirebaseAnalyticsEventParam.MESSAGE.get(), errorMessage)
                firebaseAnalytics.logEvent(name, bundle)

                // Firestore Log
                if (FirestoreManager.isLogEnable()) {
                    firestoreDB.collection(FirestoreManager.COLLECTION_LOGS)
                        .document(FirestoreManager.COLLECTION_LOGS_DOCUMENT_DATE)
                        .collection(FirestoreManager.getLogsDate())
                        .add(
                            FirestoreManager.getLogData(
                                this,
                                FirestoreLogType.MAIN_DATA_EXCEL_SAVE_ERROR,
                                errorMessage
                            )
                        )
                        .addOnSuccessListener { documentReference ->
                            LogsManager.d("MAIN_DATA_EXCEL_SAVE_ERROR addOnSuccessListener documentReference.id : ${documentReference.id}")
                        }
                        .addOnFailureListener { error ->
                            LogsManager.d("MAIN_DATA_EXCEL_SAVE_ERROR addOnFailureListener error : $error")
                        }
                }

                // FirebaseCrashlytics Log
                FirebaseCrashlyticsManager.record(
                    FirebaseCrashlyticsLogType.MAIN_DATA_EXCEL_SAVE_ERROR,
                    errorMessage
                )

                return@postDelayed
            }

            showToast(getString(R.string.app_toolBar_menu_file_save_complete))
            ProgressDialogManager.hide(this)

        }, threadDelay)
    }

    private fun handleFileLoad(uri: Uri) {
        val loadingMessage = getString(R.string.app_toolBar_menu_file_load_progress_message)
        showProgressDialog(loadingMessage)

        Handler().postDelayed({
            val thread = ExcelFileLoadThread(this, uri)
            thread.start()
            thread.join()

            val exception = thread.exception
            if (exception != null) {
                val errorFormat = getString(R.string.app_toolBar_menu_file_load_error)
                val errorMessage = String.format(errorFormat, exception)

                showAlertDialog(errorMessage)
                ProgressDialogManager.hide(this)

                // FirebaseAnalytics Log
                val name = FirebaseAnalyticsEventName.MAIN_DATA_EXCEL_LOAD_ERROR.get()
                val bundle = Bundle()
                bundle.putString(FirebaseAnalyticsEventParam.MESSAGE.get(), errorMessage)
                firebaseAnalytics.logEvent(name, bundle)

                // Firestore Log
                if (FirestoreManager.isLogEnable()) {
                    firestoreDB.collection(FirestoreManager.COLLECTION_LOGS)
                        .document(FirestoreManager.COLLECTION_LOGS_DOCUMENT_DATE)
                        .collection(FirestoreManager.getLogsDate())
                        .add(
                            FirestoreManager.getLogData(
                                this,
                                FirestoreLogType.MAIN_DATA_EXCEL_LOAD_ERROR,
                                errorMessage
                            )
                        )
                        .addOnSuccessListener { documentReference ->
                            LogsManager.d("MAIN_DATA_EXCEL_LOAD_ERROR addOnSuccessListener documentReference.id : ${documentReference.id}")
                        }
                        .addOnFailureListener { error ->
                            LogsManager.d("MAIN_DATA_EXCEL_LOAD_ERROR addOnFailureListener error : $error")
                        }
                }

                // FirebaseCrashlytics Log
                FirebaseCrashlyticsManager.record(
                    FirebaseCrashlyticsLogType.MAIN_DATA_EXCEL_LOAD_ERROR,
                    errorMessage
                )

                return@postDelayed
            }

            val dataList = thread.dataList
            handleFileLoadData(dataList)
            ProgressDialogManager.hide(this)
        }, threadDelay)
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
                showToast(getString(R.string.app_toolBar_menu_file_load_complete))
            }
            .show()

        dialog.setCanceledOnTouchOutside(false)
    }

}