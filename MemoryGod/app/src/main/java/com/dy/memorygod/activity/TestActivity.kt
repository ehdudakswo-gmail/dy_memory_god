package com.dy.memorygod.activity

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.SUCCESS
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dy.memorygod.R
import com.dy.memorygod.adapter.TestRecyclerViewAdapter
import com.dy.memorygod.adapter.TestRecyclerViewEventListener
import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.*
import com.dy.memorygod.manager.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.dialog_test_item_edit.view.*
import kotlinx.android.synthetic.main.dialog_test_item_test.view.*
import java.util.*


class TestActivity : AppCompatActivity(), TestRecyclerViewEventListener {

    private val context = this
    private val selectedData = MainDataManager.selectedData
    private val recyclerViewAdapter = TestRecyclerViewAdapter(this, this)
    private var mode = ActivityModeTest.NORMAL

    private var selectedPosition: Int = 0
    private lateinit var emptyTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var answerEditText: EditText
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        setAdMob()
        setToolbar()
        setRecyclerView()
        setOnClickListener()
        setTextToSpeech()
        showInitMessage()
        refreshMode()
    }

    private fun showProgressDialog(message: String) {
        runOnUiThread {
            ProgressDialogManager.show(this, message)
        }
    }

    private fun setAdMob() {
        MobileAds.initialize(this) {}

        val adView = adView_test_banner
        val adRequest = AdRequest.Builder().build()

        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)

                val adUnit = "test_banner"
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

    private fun setToolbar() {
        setSupportActionBar(toolbar_test)
        val actionBar = supportActionBar!!

        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)

        val title = selectedData.title
        textView_test_toolbar_title.text = title
        editText_test_toolbar_title.setText(title)
    }

    private fun setRecyclerView() {
        emptyTextView = textView_test_item_empty
        recyclerView = recyclerView_test

        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        recyclerView_test.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> {
                    KeyboardManager.hide(this, recyclerView)
                }
            }

            false
        }

        recyclerViewAdapter.init(recyclerView)
        refreshContentView(selectedData.contentList)
    }

    private fun refreshContentView(dataList: MutableList<MainDataContent>) {
        recyclerViewAdapter.refresh(dataList)
        refreshContentViewVisibility(dataList)
    }

    private fun refreshContentViewVisibility(dataList: MutableList<MainDataContent>) {
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

    private fun setOnClickListener() {
        textView_test_toolbar_title.setOnClickListener {
            when (selectedData.dataType) {
                DataType.NORMAL -> {
                    refreshMode(ActivityModeTest.TITLE_EDIT)
                }
                DataType.PHONE -> {
                    Toast.makeText(this, R.string.app_item_edit_not_allowed, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        frameLayout_test_content.setOnClickListener {
            KeyboardManager.hide(this, frameLayout_test_content)
        }
    }

    private fun setTextToSpeech() {
        textToSpeech = TextToSpeech(this) { status ->
            if (status == SUCCESS) {
                textToSpeech.language = Locale.KOREAN
            } else {
                val errorFormat = getString(R.string.app_text_to_speech_error_format)
                val errorMessage = String.format(errorFormat, status)
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showInitMessage() {
        when (selectedData.dataType) {
            DataType.NORMAL -> {
            }
            DataType.PHONE -> {
                val phoneDataMessageFormat =
                    getString(R.string.app_item_edit_not_allowed_init_message_format)
                val title = selectedData.title

                val phoneDataMessage = String.format(phoneDataMessageFormat, title)
                AlertDialogManager.show(this, phoneDataMessage)
            }
        }
    }

    private fun refreshMode() {
        val name = IntentName.ACTIVITY_MODE.get()
        val mode = intent.getSerializableExtra(name) as ActivityModeTest
        refreshMode(mode)
    }

    private fun refreshMode(mode: ActivityModeTest) {
        this.mode = mode

        clearMode()
        setMode()
    }

    private fun clearMode() {
        textView_test_toolbar_title.visibility = View.GONE
        editText_test_toolbar_title.visibility = View.GONE
        textView_test_toolbar_selection_info.visibility = View.GONE
    }

    private fun setMode() {
        when (mode) {
            ActivityModeTest.NORMAL -> {
                val editedTitle = editText_test_toolbar_title.text.toString().trim()
                selectedData.title = editedTitle

                val titleTextView = textView_test_toolbar_title
                titleTextView.text = editedTitle
                titleTextView.visibility = View.VISIBLE

                KeyboardManager.hide(this, editText_test_toolbar_title)
            }
            ActivityModeTest.SELECTION -> {
                val selectionInfoTextView = textView_test_toolbar_selection_info
                val selectionInfoFormat = getString(R.string.app_toolBar_selection_info)
                val selectionSize = recyclerViewAdapter.getSelectionSize()
                val dataSize = recyclerViewAdapter.dataList.size

                selectionInfoTextView.text =
                    String.format(selectionInfoFormat, selectionSize, dataSize)

                selectionInfoTextView.visibility = View.VISIBLE
            }
            ActivityModeTest.TITLE_EDIT -> {
                val titleEditText = editText_test_toolbar_title
                titleEditText.setText(selectedData.title)
                titleEditText.visibility = View.VISIBLE

                titleEditText.requestFocus()
                KeyboardManager.show(this)
            }
        }
    }

    override fun onBackPressed() {
        when (mode) {
            ActivityModeTest.NORMAL -> {
                handleFinish()
            }
            ActivityModeTest.SELECTION -> {
                recyclerViewAdapter.clearSelection()
            }
            ActivityModeTest.TITLE_EDIT -> {
                handleTitleEdit()
            }
        }
    }

    private fun handleFinish() {
        val dataTitle = selectedData.title
        if (dataTitle.isEmpty()) {
            Toast.makeText(
                this,
                R.string.test_toolBar_title_input_empty,
                Toast.LENGTH_SHORT
            ).show()

            refreshMode(ActivityModeTest.TITLE_EDIT)
            return
        }

        super.onBackPressed()
    }

    private fun handleTitleEdit() {
        val editedTitle = editText_test_toolbar_title.text.toString().trim()
        if (editedTitle.isEmpty()) {
            Toast.makeText(
                this,
                R.string.test_toolBar_title_input_empty,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        selectedData.title = editedTitle
        refreshMode(ActivityModeTest.NORMAL)
    }

    override fun onItemClicked(position: Int) {
        if (mode == ActivityModeTest.SELECTION) {
            return
        }

        selectedPosition = position
        val item = recyclerViewAdapter.dataList[position]
        setItemTest(item)
    }

    private fun setItemTest(data: MainDataContent) {
        val dataProblem = data.problem
        val dataAnswer = data.answer.trim()

        val view = View.inflate(this, R.layout.dialog_test_item_test, null)
        val titleLayout = view.layout_test_dialog_title
        val titleTextView = view.textView_test_dialog_title

        if (data.testCheck == TestCheck.PASS) {
            titleLayout.setBackgroundResource(data.testCheck.color)
        }
        titleTextView.text = dataProblem

        answerEditText = view.editText_test_item_test_answer
        answerEditText.requestFocus()
        KeyboardManager.show(this)

        val builder = AlertDialog.Builder(this)
        val dialog = builder
            .setView(view)
            .setPositiveButton(R.string.app_dialog_ok, null)
            .setNegativeButton(R.string.app_dialog_cancel, null)
            .setNeutralButton(R.string.test_item_test_dialog_answer_view, null)
            .show()

        dialog.setCanceledOnTouchOutside(false)
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
            Toast.makeText(
                this,
                dataAnswer,
                Toast.LENGTH_SHORT
            ).show()
        }

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val editedAnswer = answerEditText.text.toString().trim()
            if (editedAnswer.isEmpty()) {
                Toast.makeText(
                    this,
                    R.string.app_item_input_empty,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (editedAnswer != dataAnswer) {
                Toast.makeText(
                    this,
                    TestManager.getWrongResult(this, editedAnswer, dataAnswer),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            Toast.makeText(
                this,
                R.string.test_item_test_dialog_pass,
                Toast.LENGTH_SHORT
            ).show()

            data.testCheck = TestCheck.PASS
            cancelDialog(view, dialog)
        }

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            cancelDialog(view, dialog)
        }

        view.imageView_test_dialog_title_speak.setOnClickListener {
            speakVoice(dataProblem)
        }

        view.imageView_test_dialog_answer_speak.setOnClickListener {
            speakVoice(dataAnswer)
        }

        view.imageView_test_dialog_pre.setOnClickListener {
            setPreDialog(dialog)
        }

        view.imageView_test_dialog_next.setOnClickListener {
            setNextDialog(dialog)
        }

        view.imageView_test_answer_mic.setOnClickListener {
            KeyboardManager.hide(this, view)
            checkVoiceInputPermission()
        }
        view.imageView_test_answer_settings.setOnClickListener {
            showAnswerSettingsDialog()
        }
    }

    private fun speakVoice(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun setPreDialog(dialog: AlertDialog) {
        if (selectedPosition <= 0) {
            Toast.makeText(this, R.string.test_item_test_dialog_pre_limit, Toast.LENGTH_SHORT)
                .show()
        } else {
            dialog.cancel()
            selectedPosition--

            val item = recyclerViewAdapter.dataList[selectedPosition]
            setItemTest(item)
        }
    }

    private fun setNextDialog(dialog: AlertDialog) {
        val lastIdx = recyclerViewAdapter.dataList.lastIndex
        if (selectedPosition >= lastIdx) {
            Toast.makeText(this, R.string.test_item_test_dialog_next_limit, Toast.LENGTH_SHORT)
                .show()
        } else {
            dialog.cancel()
            selectedPosition++

            val item = recyclerViewAdapter.dataList[selectedPosition]
            setItemTest(item)
        }
    }

    private fun cancelDialog(view: View, dialog: AlertDialog) {
        refreshContentView(recyclerViewAdapter.dataList)
        KeyboardManager.hide(this, view)
        dialog.dismiss()
    }

    private fun checkVoiceInputPermission() {
        TedPermission.with(this)
            .setPermissionListener(voiceInputPermissionListener)
            .setPermissions(
                android.Manifest.permission.RECORD_AUDIO
            )
            .check()
    }

    private val voiceInputPermissionListener: PermissionListener =
        object : PermissionListener {
            override fun onPermissionGranted() {
                setVoiceInput()
            }

            override fun onPermissionDenied(deniedPermissions: java.util.ArrayList<String>?) {
                Toast.makeText(
                    this@TestActivity,
                    R.string.app_permission_request,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun setVoiceInput() {
        val recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                val speechMessage = getString(R.string.test_item_test_dialog_voice_speech_message)
                showProgressDialog(speechMessage)
            }

            override fun onRmsChanged(rmsdB: Float) {
            }

            override fun onBufferReceived(buffer: ByteArray?) {
            }

            override fun onPartialResults(partialResults: Bundle?) {
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
            }

            override fun onBeginningOfSpeech() {
            }

            override fun onEndOfSpeech() {
            }

            override fun onError(error: Int) {
                ProgressDialogManager.hide(this@TestActivity)
                val errorMessage = getVoiceErrorMessage(error)
                Toast.makeText(
                    this@TestActivity,
                    errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResults(results: Bundle) {
                ProgressDialogManager.hide(this@TestActivity)
                val key = SpeechRecognizer.RESULTS_RECOGNITION
                val result = results.getStringArrayList(key)

                if (result.isNullOrEmpty()) {
                    Toast.makeText(
                        this@TestActivity,
                        R.string.test_item_test_dialog_voice_result_error,
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                val data = result[0]
                answerEditText.setText(data)

                Toast.makeText(
                    this@TestActivity,
                    R.string.test_item_test_dialog_voice_result_success,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        }
        recognizer.startListening(intent)
    }

    private fun getVoiceErrorMessage(error: Int): String {
        return when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "ERROR_AUDIO"
            SpeechRecognizer.ERROR_CLIENT -> "ERROR_CLIENT"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "ERROR_INSUFFICIENT_PERMISSIONS"
            SpeechRecognizer.ERROR_NETWORK -> "ERROR_NETWORK"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "ERROR_NETWORK_TIMEOUT"
            SpeechRecognizer.ERROR_NO_MATCH -> "ERROR_NO_MATCH"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "ERROR_RECOGNIZER_BUSY"
            SpeechRecognizer.ERROR_SERVER -> "ERROR_SERVER"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "ERROR_SPEECH_TIMEOUT"
            else -> {
                val errorFormat = getString(R.string.test_item_test_dialog_voice_error_format)
                String.format(errorFormat, error)
            }
        }
    }

    private fun showAnswerSettingsDialog() {
        val itemBlankDelete = getString(R.string.test_item_test_dialog_settings_blank_delete)
        val itemDashDelete = getString(R.string.test_item_test_dialog_settings_dash_delete)

        val itemArr = arrayOf(
            itemBlankDelete,
            itemDashDelete
        )

        val builder = AlertDialog.Builder(this)
        val dialog = builder
            .setItems(itemArr) { _, which ->
                when (itemArr[which]) {
                    itemBlankDelete -> {
                        val originText = answerEditText.text.toString().trim()
                        val newText = originText.replace(" ", "")
                        answerEditText.setText(newText)
                        Toast.makeText(this, itemBlankDelete, Toast.LENGTH_SHORT).show()
                    }
                    itemDashDelete -> {
                        val originText = answerEditText.text.toString().trim()
                        val newText = originText.replace("-", "")
                        answerEditText.setText(newText)
                        Toast.makeText(this, itemDashDelete, Toast.LENGTH_SHORT).show()
                    }
                }
            }.show()
    }

    override fun onItemSelected(size: Int) {
        when (size) {
            0 -> {
                refreshMode(ActivityModeTest.NORMAL)
            }
            else -> {
                refreshMode(ActivityModeTest.SELECTION)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_test, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.test_toolBar_menu_add -> {
                handleItemAdd()
            }
            R.id.test_toolBar_menu_copy -> {
                handleItemCopy()
            }
            R.id.test_toolBar_menu_edit -> {
                handleItemEdit()
            }
            R.id.test_toolBar_menu_delete -> {
                handleItemDelete()
            }
            R.id.test_toolBar_menu_sort -> {
                handleItemSort()
            }
            R.id.test_toolBar_menu_search -> {
                handleItemSearch()
            }
            R.id.test_toolBar_menu_select_all -> {
                recyclerViewAdapter.selectAll()
            }
            R.id.test_toolBar_menu_test_check_init -> {
                handleTestCheckInit()
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
                when (ItemAddPosition.get(which)) {
                    ItemAddPosition.FIRST -> {
                        handleItem(ItemState.ADD_FIRST, MainDataContent("", "", TestCheck.NONE))
                    }
                    ItemAddPosition.LAST -> {
                        handleItem(ItemState.ADD_LAST, MainDataContent("", "", TestCheck.NONE))
                    }
                }
            }
            .show()

        dialog.setCanceledOnTouchOutside(false)
    }

    private fun handleItem(itemState: ItemState, data: MainDataContent) {
        val view = View.inflate(this, R.layout.dialog_test_item_edit, null)
        val problemEditText = view.editText_test_item_edit_problem
        val answerEditText = view.editText_test_item_edit_answer

        problemEditText.setText(data.problem)
        answerEditText.setText(data.answer)

        problemEditText.requestFocus()
        KeyboardManager.show(this)

        val title = when (itemState) {
            ItemState.ADD_FIRST -> R.string.test_item_edit_dialog_title_add
            ItemState.ADD_LAST -> R.string.test_item_edit_dialog_title_add
            ItemState.EDIT -> R.string.test_item_edit_dialog_title_edit
        }

        val builder = AlertDialog.Builder(this)
        val dialog = builder
            .setTitle(title)
            .setView(view)
            .setPositiveButton(R.string.app_dialog_ok, null)
            .setNegativeButton(R.string.app_dialog_cancel, null)
            .show()

        dialog.setCanceledOnTouchOutside(false)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val editedProblem = problemEditText.text.toString().trim()
            val editedAnswer = answerEditText.text.toString().trim()

            if (editedProblem.isEmpty() || editedAnswer.isEmpty()) {
                Toast.makeText(
                    this,
                    R.string.app_item_input_empty,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            data.problem = editedProblem
            data.answer = editedAnswer

            when (itemState) {
                ItemState.ADD_FIRST -> {
                    recyclerViewAdapter.addItemAtFirst(data)
                }
                ItemState.ADD_LAST -> {
                    recyclerViewAdapter.addItemAtLast(data)
                }
                ItemState.EDIT -> {
                }
            }

            cancelDialog(view, dialog)
        }

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            cancelDialog(view, dialog)
        }
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
            else -> {
                val selectedDataList = recyclerViewAdapter.getSelectedList()
                handleItemCopy(selectedDataList)
            }
        }
    }

    private fun handleItemCopy(selectedDataList: List<MainDataContent>) {
        val mainDataList = MainDataManager.dataList
        val dataList = mainDataList.filter { it.dataType == DataType.NORMAL }

        if (dataList.isEmpty()) {
            Toast.makeText(
                this,
                R.string.test_item_copy_position_data_empty,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val itemArr = dataList.map { it.title }.toTypedArray()
        val builder = AlertDialog.Builder(this)
        val dialog = builder
            .setTitle(R.string.test_item_copy_position_dialog_title)
            .setPositiveButton(R.string.app_dialog_ok, null)
            .setNegativeButton(R.string.app_dialog_cancel, null)
            .setSingleChoiceItems(itemArr, -1, null)
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
                    R.string.test_item_copy_position_dialog_check_empty,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            dialog.dismiss()
            handleItemCopy(checkedDataList, selectedDataList)
        }

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun handleItemCopy(
        checkedDataList: List<MainData>,
        selectedDataList: List<MainDataContent>
    ) {
        val itemArr = ItemAddPosition.getDescriptionArr(this)
        val builder = AlertDialog.Builder(this)

        val dialog = builder
            .setTitle(R.string.app_item_add_position_title)
            .setItems(itemArr) { _, which ->
                for (checkedData in checkedDataList) {
                    val copyContentDataList =
                        ItemCopyManager.createContentList(selectedDataList)

                    when (ItemAddPosition.get(which)) {
                        ItemAddPosition.FIRST -> {
                            checkedData.contentList.addAll(0, copyContentDataList)
                        }
                        ItemAddPosition.LAST -> {
                            checkedData.contentList.addAll(copyContentDataList)
                        }
                    }
                }

                refreshView()
                showCopyCompleteMessage(checkedDataList)
            }
            .show()

        dialog.setCanceledOnTouchOutside(false)
    }

    private fun showCopyCompleteMessage(checkedDataList: List<MainData>) {
        val completeInfoSeparator =
            getString(R.string.test_item_copy_complete_message_format_info_separator)
        val completeInfo = checkedDataList.joinToString(completeInfoSeparator) { it.title }
        val completeFormat = getString(R.string.test_item_copy_complete_message_format)

        val completeMessage = String.format(completeFormat, completeInfo)
        Toast.makeText(this, completeMessage, Toast.LENGTH_SHORT).show()
    }

    private fun handleItemEdit() {
        when (recyclerViewAdapter.getSelectionSize()) {
            0 -> {
                Toast.makeText(
                    this,
                    R.string.app_item_edit_selection_empty,
                    Toast.LENGTH_SHORT
                ).show()
            }
            1 -> {
                val selectedDataList = recyclerViewAdapter.getSelectedList()
                val selectedData = selectedDataList[0]
                handleItem(ItemState.EDIT, selectedData)
            }
            else -> {
                Toast.makeText(
                    this,
                    R.string.app_item_edit_selection_one_request,
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
                handleItemDelete(selectedDataList)
            }
        }
    }

    private fun handleItemDelete(selectedDataList: List<MainDataContent>) {
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
        val intent = Intent(this@TestActivity, TestSortActivity::class.java)
        startActivityForResult(intent, RequestCode.TEST_SORT.get())
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
        val intent = Intent(this@TestActivity, TestSearchActivity::class.java)
        startActivityForResult(intent, RequestCode.TEST_SEARCH.get())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RequestCode.TEST_SORT.get() -> {
                recyclerViewAdapter.notifyDataSetChanged()
                recyclerViewAdapter.scrollToPosition(0)
            }
            RequestCode.TEST_SEARCH.get() -> {
                val searchData = MainDataManager.searchContentData ?: return
                recyclerViewAdapter.select(searchData)
            }
        }
    }

    private fun handleTestCheckInit() {
        when (recyclerViewAdapter.getSelectionSize()) {
            0 -> {
                Toast.makeText(
                    this,
                    R.string.test_item_test_check_init_selection_empty,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                val selectedDataList = recyclerViewAdapter.getSelectedList()
                for (selectedData in selectedDataList) {
                    selectedData.testCheck = TestCheck.NONE
                }

                refreshView()
            }
        }
    }

    private fun refreshView() {
        refreshContentView(recyclerViewAdapter.dataList)
        recyclerViewAdapter.clearSelection()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (this::textToSpeech.isInitialized) {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

}