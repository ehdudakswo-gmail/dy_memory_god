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
import com.dy.memorygod.adapter.TestRecyclerViewAdapter
import com.dy.memorygod.adapter.TestRecyclerViewEventListener
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.*
import com.dy.memorygod.manager.KeyboardManager
import com.dy.memorygod.manager.MainDataManager
import com.dy.memorygod.manager.TestManager
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.dialog_test_item_edit.view.*
import kotlinx.android.synthetic.main.dialog_test_item_test.view.*


class TestActivity : AppCompatActivity(), TestRecyclerViewEventListener {

    private val selectedData = MainDataManager.selectedData
    private val recyclerViewAdapter = TestRecyclerViewAdapter(this, this)
    private var mode = ActivityModeTest.NORMAL

    private lateinit var emptyTextView: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        setToolbar()
        setRecyclerView()
        setOnClickListener()
        refreshMode()
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

        val item = recyclerViewAdapter.dataList[position]
        setItemTest(item)
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
    }

    private fun cancelDialog(view: View, dialog: AlertDialog) {
        refreshContentView(recyclerViewAdapter.dataList)
        KeyboardManager.hide(this, view)
        dialog.dismiss()
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
            R.id.test_toolBar_menu_edit -> {
                when (recyclerViewAdapter.getSelectionSize()) {
                    0 -> {
                        Toast.makeText(
                            this,
                            R.string.app_item_edit_selection_empty,
                            Toast.LENGTH_SHORT
                        )
                            .show()
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
            R.id.test_toolBar_menu_delete -> {
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
            R.id.test_toolBar_menu_sort -> {
                if (recyclerViewAdapter.itemCount == 0) {
                    Toast.makeText(
                        this,
                        R.string.app_toolBar_menu_sort_data_empty,
                        Toast.LENGTH_SHORT
                    ).show()
                    return true
                }

                recyclerViewAdapter.clearSelection()
                val intent = Intent(this@TestActivity, TestSortActivity::class.java)
                startActivityForResult(intent, RequestCode.TEST_SORT.get())
            }
            R.id.test_toolBar_menu_search -> {
                if (recyclerViewAdapter.itemCount == 0) {
                    Toast.makeText(
                        this,
                        R.string.app_toolBar_menu_search_data_empty,
                        Toast.LENGTH_SHORT
                    ).show()
                    return true
                }

                recyclerViewAdapter.clearSelection()
                val intent = Intent(this@TestActivity, TestSearchActivity::class.java)
                startActivityForResult(intent, RequestCode.TEST_SEARCH.get())
            }
            R.id.test_toolBar_menu_select_all -> {
                recyclerViewAdapter.selectAll()
            }
            R.id.test_toolBar_menu_test_check_init -> {
                when (recyclerViewAdapter.getSelectionSize()) {
                    0 -> {
                        Toast.makeText(
                            this,
                            R.string.test_item_test_check_init_selection_empty,
                            Toast.LENGTH_SHORT
                        )
                            .show()
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

}