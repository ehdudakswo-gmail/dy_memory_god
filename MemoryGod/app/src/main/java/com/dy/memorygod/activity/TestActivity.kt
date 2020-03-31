package com.dy.memorygod.activity

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dy.memorygod.R
import com.dy.memorygod.adapter.TestRecyclerViewAdapter
import com.dy.memorygod.adapter.TestRecyclerViewEventListener
import com.dy.memorygod.adapter.TestRecyclerViewTouchHelperCallback
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.ActivityMode
import com.dy.memorygod.enums.DataType
import com.dy.memorygod.enums.TestCheck
import com.dy.memorygod.manager.ContactManager
import com.dy.memorygod.manager.KeyboardManager
import com.dy.memorygod.manager.MainDataManager
import com.dy.memorygod.manager.TestManager
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.dialog_test_item_edit.view.*
import kotlinx.android.synthetic.main.dialog_test_item_test.view.*
import java.util.*


class TestActivity : AppCompatActivity(), TestRecyclerViewEventListener {

    private val selectedMainData = MainDataManager.selectedData
    private var mode = ActivityMode.TEST_NORMAL

    private val recyclerViewAdapter = TestRecyclerViewAdapter(this, this)
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        checkDataType()
    }

    private fun checkDataType() {
        when (selectedMainData.dataType) {
            DataType.Normal -> {
                initActivity()
            }
            DataType.PhoneNumber -> {
                checkPermissions()
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
                if (handlePhoneData()) {
                    selectedMainData.updatedDate = Date()
                    initActivity()
                }
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                Toast.makeText(
                    this@TestActivity,
                    R.string.app_permission_need, Toast.LENGTH_LONG
                )
                    .show()
                finish()
            }
        }

    private fun handlePhoneData(): Boolean {
        return when (selectedMainData.dataType) {
            DataType.PhoneNumber -> setPhoneNumberList()
            else -> false
        }
    }

    private fun setPhoneNumberList(): Boolean {
        val phoneNumberList = ContactManager.getPhoneNumberList(this)
        if (phoneNumberList == ContactManager.ERROR_CONTACT_PHONE_NUMBER) {
            Toast.makeText(this, ContactManager.ERROR_MESSAGE, Toast.LENGTH_LONG).show()
            finish()
            return false
        }

        val contentList = selectedMainData.contentList
        contentList.clear()

        for (phoneNumber in phoneNumberList) {
            val title = phoneNumber.name
            val data = phoneNumber.phoneNumber

            val content = MainDataContent(title, data)
            contentList.add(content)
        }

        return true
    }

    private fun initActivity() {
        setToolbar()
        setRecyclerView()
        setSearchView()
        setItemTouchHelper()
        setOnClickListener()
        refreshView(ActivityMode.TEST_NORMAL)
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_test)
        val actionBar = supportActionBar!!

        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(false)

        val title = selectedMainData.title
        textView_test_toolbar_title.text = title
        editText_test_toolbar_title.setText(title)
    }

    private fun setRecyclerView() {
        val recyclerView = recyclerView_test
        val layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        recyclerView_test.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> {
                    searchView_test.clearFocus()
                }
            }

            false
        }
    }

    private fun setSearchView() {
        searchView_test.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val text = newText!!.trim()
                val filterList =
                    selectedMainData.contentList.filter { it.problem.contains(text) }
                        .toMutableList()

                refreshContentView(filterList)
                return true
            }
        })
    }

    private fun setItemTouchHelper() {
        val callBack = TestRecyclerViewTouchHelperCallback(recyclerViewAdapter)
        itemTouchHelper = ItemTouchHelper(callBack)
        itemTouchHelper.attachToRecyclerView(recyclerView_test)
    }

    private fun setOnClickListener() {
        frameLayout_test_toolbar_edit_flag.setOnClickListener {
            when (selectedMainData.dataType) {
                DataType.PhoneNumber -> {
                    Toast.makeText(this, R.string.test_edit_not_allowed, Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    changeView()
                }
            }
        }

        floatingActionButton_test_item_add.setOnClickListener {
            setItemEdit(
                MainDataContent("", ""),
                true
            )
        }

        frameLayout_test_toolbar_edit_mode_trash.setOnClickListener {
            showTrashList()
        }

        frameLayout_test_content.setOnClickListener {
            KeyboardManager.hide(this, frameLayout_test_content)
        }
    }

    private fun changeView() {
        when (mode) {
            ActivityMode.TEST_NORMAL -> {
                refreshView(ActivityMode.TEST_EDIT)
            }
            ActivityMode.TEST_EDIT -> {
                checkEditComplete()
            }
        }
    }

    private fun refreshView(mode: ActivityMode) {
        this.mode = mode
        refreshMode()
        refreshContentView(selectedMainData.contentList)
        searchView_test.setQuery("", false)
    }

    private fun refreshMode() {
        when (mode) {
            ActivityMode.TEST_NORMAL -> {
                setNormalMode()
            }
            ActivityMode.TEST_EDIT -> {
                setEditMode()
            }
        }
    }

    private fun setNormalMode() {
        textView_test_toolbar_title.visibility = View.VISIBLE
        editText_test_toolbar_title.visibility = View.GONE

        val editedTitle = editText_test_toolbar_title.text.toString().trim()
        textView_test_toolbar_title.text = editedTitle
        selectedMainData.title = editedTitle

        imageView_test_toolbar_edit_mode.visibility = View.VISIBLE
        imageView_test_toolbar_edit_complete.visibility = View.GONE
        frameLayout_test_toolbar_edit_mode_trash.visibility = View.GONE

        searchView_test.setQuery("", false)
        searchView_test.visibility = View.VISIBLE

        floatingActionButton_test_item_add.visibility = View.GONE
        KeyboardManager.hide(this, editText_test_toolbar_title)
    }

    private fun setEditMode() {
        textView_test_toolbar_title.visibility = View.GONE
        editText_test_toolbar_title.visibility = View.VISIBLE

        val dataTitle = selectedMainData.title
        editText_test_toolbar_title.setText(dataTitle)

        imageView_test_toolbar_edit_mode.visibility = View.GONE
        imageView_test_toolbar_edit_complete.visibility = View.VISIBLE
        frameLayout_test_toolbar_edit_mode_trash.visibility = View.VISIBLE

        searchView_test.setQuery("", false)
        searchView_test.visibility = View.GONE

        floatingActionButton_test_item_add.visibility = View.VISIBLE
    }

    private fun refreshContentView(dataList: MutableList<MainDataContent>) {
        recyclerViewAdapter.refresh(mode, dataList)
        refreshContentViewVisibility(dataList)
    }

    private fun refreshContentViewVisibility(dataList: MutableList<MainDataContent>) {
        when (dataList.count()) {
            0 -> {
                textView_test_content.visibility = View.VISIBLE
                recyclerView_test.visibility = View.GONE
            }
            else -> {
                textView_test_content.visibility = View.GONE
                recyclerView_test.visibility = View.VISIBLE
            }
        }
    }

    private fun setItemEdit(data: MainDataContent, isAdd: Boolean) {
        val view = View.inflate(this, R.layout.dialog_test_item_edit, null)
        val problemEditText = view.editText_test_item_edit_problem
        val answerEditText = view.editText_test_item_edit_answer

        problemEditText.setText(data.problem)
        answerEditText.setText(data.answer)

        problemEditText.requestFocus()
        KeyboardManager.show(this)

        val title = when (isAdd) {
            true -> R.string.test_item_edit_dialog_title_add
            false -> R.string.test_item_edit_dialog_title_edit
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
                    R.string.app_input_empty,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            data.problem = editedProblem
            data.answer = editedAnswer

            if (isAdd) {
                recyclerViewAdapter.addItem(data)
            }

            cancelDialog(view, dialog)
        }

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            cancelDialog(view, dialog)
        }
    }

    private fun cancelDialog(view: View, dialog: AlertDialog) {
        refreshContentView(recyclerViewAdapter.dataList)
        KeyboardManager.hide(this, view)
        dialog.dismiss()
    }

    private fun showTrashList() {
        val trashList = recyclerViewAdapter.trashList
        val trashCount = trashList.count()

        val titleFormat = getString(R.string.test_trash_title_format)
        val title = String.format(titleFormat, trashCount)

        val itemArr = arrayOfNulls<String>(trashList.size)
        for (i in trashList.indices) {
            itemArr[i] = trashList[i].problem
        }

        val builder = AlertDialog.Builder(this)
        val dialog = builder
            .setTitle(title)
            .setItems(itemArr) { _, which ->
                val selectedItem = trashList[which]
                recyclerViewAdapter.restoreItem(selectedItem)
                refreshContentView(recyclerViewAdapter.dataList)
            }
            .show()

        dialog.setOnCancelListener {

        }
    }

    override fun onItemClicked(position: Int) {
        val item = recyclerViewAdapter.dataList[position]

        when (mode) {
            ActivityMode.TEST_NORMAL -> {
                setItemTest(item)
            }
            ActivityMode.TEST_EDIT -> {
                setItemEdit(item, false)
            }
        }
    }

    private fun setItemTest(data: MainDataContent) {
        val view = View.inflate(this, R.layout.dialog_test_item_test, null)
        val answerEditText = view.editText_test_item_test_answer

        answerEditText.requestFocus()
        KeyboardManager.show(this)

        val title = data.problem
        val dataAnswer = data.answer.trim()

        val builder = AlertDialog.Builder(this)
        val dialog = builder
            .setTitle(title)
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
                    R.string.app_input_empty,
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (editedAnswer != dataAnswer) {
                Toast.makeText(
                    this,
                    TestManager.getWrongReason(this, editedAnswer, dataAnswer),
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
    }

    override fun onItemLongClicked(position: Int) {
    }

    override fun onItemDeleted(position: Int) {
        recyclerViewAdapter.deleteItem(position)
        refreshContentView(recyclerViewAdapter.dataList)
    }

    override fun onDragStarted(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onBackPressed() {
        when (mode) {
            ActivityMode.TEST_NORMAL -> {
                if (searchView_test.query.trim().isNotEmpty()) {
                    searchView_test.setQuery("", false)
                    searchView_test.clearFocus()
                    return
                }

                super.onBackPressed()
            }
            ActivityMode.TEST_EDIT -> {
                checkEditComplete()
            }
        }
    }

    private fun checkEditComplete() {
        val toolbarTitle = editText_test_toolbar_title.text.trim()
        if (toolbarTitle.isEmpty()) {
            Toast.makeText(
                this,
                R.string.test_toolBar_title_input_empty,
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val trashCount = recyclerViewAdapter.trashList.count()
        if (trashCount > 0) {
            val messageFormat = getString(R.string.test_trash_check_dialog_message_format)
            val message = String.format(messageFormat, trashCount)

            val builder = AlertDialog.Builder(this)
            val dialog = builder
                .setMessage(message)
                .setPositiveButton(R.string.app_dialog_ok, null)
                .setNegativeButton(R.string.app_dialog_cancel, null)
                .show()

            dialog.setCanceledOnTouchOutside(false)
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                setEditComplete()
                dialog.dismiss()
            }

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
                dialog.dismiss()
            }

            dialog.setOnCancelListener {

            }
        } else {
            setEditComplete()
        }
    }

    private fun setEditComplete() {
        recyclerViewAdapter.trashList.clear()
        refreshView(ActivityMode.TEST_NORMAL)
    }

}