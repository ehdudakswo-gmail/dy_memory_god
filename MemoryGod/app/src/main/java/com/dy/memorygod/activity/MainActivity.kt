package com.dy.memorygod.activity

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
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
import com.dy.memorygod.retrofit.RetrofitClient
import com.dy.memorygod.retrofit.RetrofitErrorMessage
import com.dy.memorygod.retrofit.RetrofitService
import com.dy.memorygod.thread.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), MainRecyclerViewEventListener {

    private val context = this
    private val recyclerViewAdapter = MainRecyclerViewAdapter(this, this)
    private var mode = ActivityModeMain.NORMAL

    private lateinit var emptyTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private val threadDelay = 100L

    private var backPressedTime: Long = 0
    private val backPressedTimeInterval: Long = 2000

    // firebase
    private var firestoreConfigSnapshotListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setToolbar()
        setFirestoreConfig()
        setAdMob()
        setData()
    }

    override fun onStart() {
        super.onStart()

        if (MainDataManager.isLoadingComplete) {
            val dataList = recyclerViewAdapter.dataList
            refreshContentView(dataList)
            saveBackupData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        ProgressDialogManager.hide(this)
        AlertDialogManager.hide(this)

        firestoreConfigSnapshotListener?.remove()
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

    private fun hideProgressDialog() {
        runOnUiThread {
            ProgressDialogManager.hide(this)
        }
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_main)
        val actionBar = supportActionBar!!

        actionBar.setDisplayShowTitleEnabled(false)

        textView_main_toolbar_title.setOnLongClickListener {
            // show message
            val appInfo = GlobalApplication.instance.getAppInfo()
            showAlertDialog(appInfo)

            // copy clipboard
            val clipboardManager = getSystemService(Activity.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("appInfo", appInfo)
            clipboardManager.setPrimaryClip(clipData)

            true
        }
    }

    private fun setFirestoreConfig() {
        val db = FirebaseFirestore.getInstance()
        val collectionPath = FirebaseFirestoreManager.CONFIG
        val documentPath = FirebaseFirestoreManager.CONFIG_ANDROID
        val docRef = db.collection(collectionPath).document(documentPath)

        // init
        docRef
            .get()
            .addOnSuccessListener { document ->
                val data = document.data
                if (data == null) {
                    val logMessage = "setFirestoreConfig addOnSuccessListener data null error"
                    FirebaseLogManager.logFirestoreError(this, logMessage)
                    return@addOnSuccessListener
                }

                setAppConfig(data)
            }
            .addOnFailureListener { exception ->
                val logMessage = "setFirestoreConfig addOnFailureListener exception : $exception"
                FirebaseLogManager.logFirestoreError(this, logMessage)
            }

        // realtime
        firestoreConfigSnapshotListener = docRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                val logMessage = "setFirestoreConfig addSnapshotListener exception : $exception"
                FirebaseLogManager.logFirestoreError(this, logMessage)
                return@addSnapshotListener
            }

            if (snapshot == null || !snapshot.exists()) {
                val logMessage = "setFirestoreConfig addSnapshotListener snapshot null error"
                FirebaseLogManager.logFirestoreError(this, logMessage)
                return@addSnapshotListener
            }

            val data = snapshot.data
            if (data == null) {
                val logMessage = "setFirestoreConfig addSnapshotListener data null error"
                FirebaseLogManager.logFirestoreError(this, logMessage)
                return@addSnapshotListener
            }

            setAppConfig(data)
        }
    }

    private fun setAppConfig(data: Map<String, Any>) {
        val logEnable = data["logEnable"] as Boolean
        val logStopTypes = data["logStopTypes"] as List<*>
        val shareDataEnable = data["shareDataEnable"] as Boolean

        val appConfig = GlobalApplication.instance.firestoreConfig
        appConfig.logEnable = logEnable
        appConfig.logStopTypes = logStopTypes
        appConfig.shareDataEnable = shareDataEnable
    }

    private fun setAdMob() {
        MobileAds.initialize(this) {}

        val adView = adView_main_banner
        val adRequest = AdRequest.Builder().build()

        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)

                val adUnit = "main_banner"
                val errorCode = error.code
                val errorMessage = error.message

                val logMessageArr =
                    arrayOf(
                        "adUnit : $adUnit",
                        "errorCode : $errorCode",
                        "errorMessage : $errorMessage"
                    )

                // Log Data
                val logType = LogType.ADMOB_FAILED_TO_LOAD
                val logMessage = FirebaseLogManager.getJoinData(logMessageArr)

                // Firebase Log
                FirebaseLogManager.log(context, logType, logMessage)
            }
        }
    }

    private fun setData() {
        val progressMessage = getString(R.string.app_data_load_progress_message)
        showProgressDialog(progressMessage)
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
        AndroidLogManager.d("setData backupData exception: $exception")

        if (exception != null) {
            // Log Data
            val logType = LogType.BACKUP_DATA_LOAD_ERROR
            val logMessage = exception

            // Show Message
            showToast(logType.get())

            // Firebase Log
            FirebaseLogManager.log(this, logType, logMessage)
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
            AndroidLogManager.d("loadSampleData exception : $exception")

            if (exception != null) {
                // Log Data
                val logType = LogType.SAMPLE_DATA_LOAD_ERROR
                val logMessage = exception

                // Show Message
                showToast(logType.get())

                // Firebase Log
                FirebaseLogManager.log(this, logType, logMessage)
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
                // Log Data
                val logType = LogType.MAIN_DATA_DB_LOAD_ERROR
                val logMessage = exception

                // Show Message
                showToast(logType.get())

                // Firebase Log
                FirebaseLogManager.log(this, logType, logMessage)
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
    }

    private fun startTest(data: MainData, activityMode: ActivityModeTest) {
        // activity
        data.updatedDate = Date()
        MainDataManager.selectedData = data

        val intent = Intent(this@MainActivity, TestActivity::class.java)
        val name = IntentName.ACTIVITY_MODE.get()

        intent.putExtra(name, activityMode)
        startActivity(intent)

        // log
        val logMessageArr = arrayOf(
            "title : ${getTitleLog(data)}",
            "content : ${getContentLog(data)}"
        )

        val logType = LogType.MAIN_ITEM_CLICK
        val logMessage = FirebaseLogManager.getJoinData(logMessageArr)
        FirebaseLogManager.log(this, logType, logMessage)
    }

    private fun getTitleLog(data: MainData): String {
        val title = data.title

        if (title.isEmpty()) {
            return FirebaseLogManager.DATA_EMPTY
        }

        return title
    }

    private fun getContentLog(data: MainData): String {
        val contentList = data.contentList

        if (contentList.isEmpty()) {
            return FirebaseLogManager.DATA_EMPTY
        }

        val size = contentList.size
        val first = contentList.first().problem
        val last = contentList.last().problem

        return "${size}개 ($first ~ $last)"
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
                    refreshContentView(MainDataManager.dataList)

                    // Log Data
                    val logType = LogType.PHONE_NUMBER_LOAD_ERROR
                    val logMessage = ContactManager.ERROR_MESSAGE

                    // Show Message
                    showAlertDialog(logType.get())

                    // Firebase Log
                    FirebaseLogManager.log(context, logType, logMessage)

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
                handleBackPressedTwice()
            }
            ActivityModeMain.SELECTION -> {
                recyclerViewAdapter.clearSelection()
            }
        }
    }

    private fun handleBackPressedTwice() {
        if (System.currentTimeMillis() - backPressedTime < backPressedTimeInterval) {
            finishWithBackup()
            return
        }

        backPressedTime = System.currentTimeMillis()
        showToast(getString(R.string.app_back_press_finish))
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
                // Log Data
                val logType = LogType.MAIN_DATA_DB_SAVE_ERROR
                val logMessage = exception

                // Show Message
                showToast(logType.get())

                // Firebase Log
                FirebaseLogManager.log(this, logType, logMessage)
            }

            super.onBackPressed()
        }, threadDelay)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val shareDataDownload =
            menu.findItem(R.id.main_toolBar_menu_share_data_download) ?: return true

        val appConfig = GlobalApplication.instance.firestoreConfig
        val shareDataEnable = appConfig.shareDataEnable
        shareDataDownload.isVisible = shareDataEnable

        return super.onPrepareOptionsMenu(menu)
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
            R.id.main_toolBar_menu_share_data_download -> {
                handleShareData()
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
            // Log Data
            val logType = LogType.PHONE_NUMBER_REFRESH_ERROR
            val logMessage = ContactManager.ERROR_MESSAGE

            // Show Message
            showToast(logType.get())

            // Firebase Log
            FirebaseLogManager.log(this, logType, logMessage)

            return
        }

        setPhoneNumber(data, phoneNumberList)
    }

    private fun refreshView() {
        refreshContentView(recyclerViewAdapter.dataList)
        recyclerViewAdapter.clearSelection()
    }

    private fun handleShareData() {
        val appConfig = GlobalApplication.instance.firestoreConfig
        val shareDataEnable = appConfig.shareDataEnable

        if (!shareDataEnable) {
            val stopMessage = getString(R.string.app_service_stop)
            showToast(stopMessage)
            return
        }

        getShareList()
    }

    private fun getShareList() {
        val progressMessage =
            getString(R.string.app_toolBar_menu_share_list_progress_dialog_message)
        showProgressDialog(progressMessage)

        val service = RetrofitClient.getService().getShareList()
        val logType = LogType.SHARE_LIST_ERROR

        service.enqueue(object : Callback<RetrofitService.ShareListResponse> {
            override fun onResponse(
                call: Call<RetrofitService.ShareListResponse>,
                response: Response<RetrofitService.ShareListResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody == null) {
                        showAlertDialog(logType.get())
                        hideProgressDialog()

                        val logMessage = RetrofitErrorMessage.onResponseBodyNull()
                        FirebaseLogManager.log(context, logType, logMessage)
                        return
                    }

                    val error = responseBody.error
                    if (error != null) {
                        showAlertDialog(logType.get())
                        hideProgressDialog()

                        val logMessage = RetrofitErrorMessage.onResponseError(error)
                        FirebaseLogManager.log(context, logType, logMessage)
                        return
                    }

                    val data = responseBody.data
                    if (data == null) {
                        showAlertDialog(logType.get())
                        hideProgressDialog()

                        val logMessage = RetrofitErrorMessage.onResponseDataNull()
                        FirebaseLogManager.log(context, logType, logMessage)
                        return
                    }

                    showShareList(data)
                    hideProgressDialog()
                } else {
                    showAlertDialog(logType.get())
                    hideProgressDialog()

                    val responseCode = response.code()
                    val logMessage = RetrofitErrorMessage.onResponseFailed(responseCode)
                    FirebaseLogManager.log(context, logType, logMessage)
                }
            }

            override fun onFailure(
                call: Call<RetrofitService.ShareListResponse>,
                t: Throwable
            ) {
                showAlertDialog(logType.get())
                hideProgressDialog()

                val logMessage = RetrofitErrorMessage.onFailure(t)
                FirebaseLogManager.log(context, logType, logMessage)
            }
        })
    }

    private fun showShareList(shareList: List<String>) {
        val items = shareList.map { it }.toTypedArray()
        val builder = AlertDialog.Builder(this)
        val dialog = builder
            .setTitle(R.string.app_toolBar_menu_share_list_choice_dialog_title)
            .setPositiveButton(R.string.app_dialog_ok, null)
            .setNegativeButton(R.string.app_dialog_cancel, null)
            .setSingleChoiceItems(items, -1, null)
            .show()

        dialog.setCanceledOnTouchOutside(false)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val listView = dialog.listView
            val checkedList = mutableListOf<String>()

            for (i in items.indices) {
                if (listView.isItemChecked(i)) {
                    val checkedItem = items[i]
                    checkedList.add(checkedItem)
                }
            }

            if (checkedList.isEmpty()) {
                showToast(getString(R.string.app_dialog_check_empty))
                return@setOnClickListener
            }

            val checkedItem = checkedList[0]
            getShareData(checkedItem)
            dialog.dismiss()
        }
    }

    private fun getShareData(sheetName: String) {
        val progressMessageFormat =
            getString(R.string.app_toolBar_menu_share_data_progress_dialog_message_format)
        val progressMessage = String.format(progressMessageFormat, sheetName)
        showProgressDialog(progressMessage)

        val service = RetrofitClient.getService().getShareData(sheetName)
        val logType = LogType.SHARE_DATA_ERROR

        service.enqueue(object : Callback<RetrofitService.ShareDataResponse> {
            override fun onResponse(
                call: Call<RetrofitService.ShareDataResponse>,
                response: Response<RetrofitService.ShareDataResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody == null) {
                        showAlertDialog(logType.get())
                        hideProgressDialog()

                        val retrofitError = RetrofitErrorMessage.onResponseBodyNull()
                        val logMessage = getShareDataErrorLogMessage(sheetName, retrofitError)
                        FirebaseLogManager.log(context, logType, logMessage)
                        return
                    }

                    val error = responseBody.error
                    if (error != null) {
                        showAlertDialog(logType.get())
                        hideProgressDialog()

                        val retrofitError = RetrofitErrorMessage.onResponseError(error)
                        val logMessage = getShareDataErrorLogMessage(sheetName, retrofitError)
                        FirebaseLogManager.log(context, logType, logMessage)
                        return
                    }

                    val data = responseBody.data
                    if (data == null) {
                        showAlertDialog(logType.get())
                        hideProgressDialog()

                        val retrofitError = RetrofitErrorMessage.onResponseDataNull()
                        val logMessage = getShareDataErrorLogMessage(sheetName, retrofitError)
                        FirebaseLogManager.log(context, logType, logMessage)
                        return
                    }

                    try {
                        setShareData(sheetName, data)
                        refreshView()
                        handleShareDataDownloadSuccess(sheetName, data)
                    } catch (ex: Exception) {
                        showAlertDialog(logType.get())
                        val retrofitError = RetrofitErrorMessage.onResponseException(ex)
                        val logMessage = getShareDataErrorLogMessage(sheetName, retrofitError)
                        FirebaseLogManager.log(context, logType, logMessage)
                    } finally {
                        hideProgressDialog()
                    }
                } else {
                    showAlertDialog(logType.get())
                    hideProgressDialog()

                    val responseCode = response.code()
                    val retrofitError = RetrofitErrorMessage.onResponseFailed(responseCode)
                    val logMessage = getShareDataErrorLogMessage(sheetName, retrofitError)
                    FirebaseLogManager.log(context, logType, logMessage)
                }
            }

            override fun onFailure(
                call: Call<RetrofitService.ShareDataResponse>,
                t: Throwable
            ) {
                showAlertDialog(logType.get())
                hideProgressDialog()

                val retrofitError = RetrofitErrorMessage.onFailure(t)
                val logMessage = getShareDataErrorLogMessage(sheetName, retrofitError)
                FirebaseLogManager.log(context, logType, logMessage)
            }
        })
    }

    private fun getShareDataErrorLogMessage(sheetName: String, retrofitError: String): String {
        val logMessageArr = arrayOf(
            "sheetName : $sheetName",
            "retrofitError : $retrofitError"
        )

        return FirebaseLogManager.getJoinData(logMessageArr)
    }

    private fun setShareData(sheetName: String, shareData: List<List<String>>) {
        val contentList = mutableListOf<MainDataContent>()
        for (low in shareData) {
            val problemIdx = MainDataContentHeader.PROBLEM.getIdx()
            val answerIdx = MainDataContentHeader.ANSWER.getIdx()

            val problem = low[problemIdx]
            val answer = low[answerIdx]

            val content = MainDataContent(problem, answer, TestCheck.NONE)
            contentList.add(content)
        }

        val mainData =
            MainData(sheetName, contentList, Date(), DataType.NORMAL, DataTypePhone.NONE)
        MainDataManager.dataList.add(mainData)
    }

    private fun handleShareDataDownloadSuccess(sheetName: String, data: List<List<String>>) {
        val messageFormat =
            getString(R.string.app_toolBar_menu_share_data_download_success_message_format)
        val message = String.format(messageFormat, sheetName)
        showToast(message)

        val logMessageArr = arrayOf(
            "sheetName : $sheetName",
            "data.size : ${data.size}"
        )
        val logType = LogType.SHARE_DATA_DOWNLOAD_SUCCESS
        val logMessage = FirebaseLogManager.getJoinData(logMessageArr)
        FirebaseLogManager.log(this, logType, logMessage)
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
                // Log Data
                val logType = LogType.MAIN_DATA_EXCEL_SAVE_ERROR
                val logMessage = exception

                // Show Message
                showAlertDialog(logType.get())
                ProgressDialogManager.hide(this)

                // Firebase Log
                FirebaseLogManager.log(this, logType, logMessage)

                return@postDelayed
            }

            showToast(getString(R.string.app_toolBar_menu_file_save_complete))
            ProgressDialogManager.hide(this)

            /** LOG START **/
            // Log Data
            val logType = LogType.MAIN_DATA_EXCEL_SAVE_SUCCESS
            val logMessage = "uri : $uri"

            // Firebase Log
            FirebaseLogManager.log(this, logType, logMessage)
            /** LOG END **/
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
                // Log Data
                val logType = LogType.MAIN_DATA_EXCEL_LOAD_ERROR
                val logMessage = exception

                // Show Message
                showAlertDialog(logType.get())
                ProgressDialogManager.hide(this)

                // Firebase Log
                FirebaseLogManager.log(this, logType, logMessage)

                return@postDelayed
            }

            val dataList = thread.dataList
            handleFileLoadData(dataList)
            ProgressDialogManager.hide(this)

            /** LOG START **/
            // Log Data
            val logType = LogType.MAIN_DATA_EXCEL_LOAD_SUCCESS
            val logMessage = "uri : $uri"

            // Firebase Log
            FirebaseLogManager.log(this, logType, logMessage)
            /** LOG END **/
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