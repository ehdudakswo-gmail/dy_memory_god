package com.dy.memorygod.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

    private val selectedMainData = MainDataManager.selectedData
    private val recyclerViewAdapter = TestRecyclerViewAdapter(this, this)
    private var mode = ActivityMode.TEST_NORMAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        setToolbar()
        setRecyclerView()
        setOnClickListener()
        refreshMode(ActivityMode.TEST_NORMAL)
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_test)
        val actionBar = supportActionBar!!

        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)

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

        recyclerView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> {
                    KeyboardManager.hide(this, recyclerView)
                }
            }

            false
        }

        recyclerViewAdapter.init(recyclerView)
        refreshContentView(selectedMainData.contentList)
    }

    private fun refreshContentView(dataList: MutableList<MainDataContent>) {
        recyclerViewAdapter.refresh(dataList)
        refreshContentViewVisibility(dataList)
    }

    private fun refreshContentViewVisibility(dataList: MutableList<MainDataContent>) {
        val emptyTextView = textView_test_item_empty
        val recyclerView = recyclerView_test

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
            refreshMode(ActivityMode.TEST_TITLE_EDIT)
        }

        frameLayout_test_content.setOnClickListener {
            KeyboardManager.hide(this, frameLayout_test_content)
        }

        floatingActionButton_test_item_add.setOnClickListener {
            when (selectedMainData.dataType) {
                DataType.NORMAL -> {
                    handelItem(
                        ItemState.ADD,
                        MainDataContent("", "")
                    )
                }
                else -> {
                    Toast.makeText(this, R.string.app_item_edit_not_allowed, Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun refreshMode(mode: ActivityMode) {
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
            ActivityMode.TEST_NORMAL -> {
                val titleTextView = textView_test_toolbar_title
                titleTextView.text = selectedMainData.title
                titleTextView.visibility = View.VISIBLE

                KeyboardManager.hide(this, editText_test_toolbar_title)
            }
            ActivityMode.TEST_TITLE_EDIT -> {
                val titleEditText = editText_test_toolbar_title
                titleEditText.setText(selectedMainData.title)
                titleEditText.visibility = View.VISIBLE

                titleEditText.requestFocus()
                KeyboardManager.show(this)
            }
            ActivityMode.TEST_SELECTION -> {
                val selectionInfoTextView = textView_test_toolbar_selection_info
                val selectionInfoFormat = getString(R.string.app_toolBar_selection_info)
                val selectionSize = recyclerViewAdapter.getSelectionSize()
                val dataSize = recyclerViewAdapter.dataList.size

                selectionInfoTextView.text =
                    String.format(selectionInfoFormat, selectionSize, dataSize)

                selectionInfoTextView.visibility = View.VISIBLE
            }
        }
    }


    override fun onBackPressed() {
        when (mode) {
            ActivityMode.TEST_NORMAL -> {
                super.onBackPressed()
            }
            ActivityMode.TEST_TITLE_EDIT -> {
                handleTitleEdit()
            }
            ActivityMode.TEST_SELECTION -> {
                recyclerViewAdapter.clearSelection()
            }
        }
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

        selectedMainData.title = editedTitle
        refreshMode(ActivityMode.TEST_NORMAL)
    }

    override fun onItemClicked(position: Int) {
        if (mode == ActivityMode.TEST_SELECTION) {
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
                refreshMode(ActivityMode.TEST_NORMAL)
            }
            else -> {
                refreshMode(ActivityMode.TEST_SELECTION)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_test, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (isNotEditable(item)) {
            Toast.makeText(this, R.string.app_item_edit_not_allowed, Toast.LENGTH_SHORT).show()
            return true
        }

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.test_toolBar_menu_add -> {
                handelItem(
                    ItemState.ADD,
                    MainDataContent("", "")
                )
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
                        val selectedItem = selectedDataList[0]
                        handelItem(ItemState.EDIT, selectedItem)
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

    private fun isNotEditable(item: MenuItem): Boolean {
        return when (selectedMainData.dataType) {
            DataType.NORMAL -> {
                false
            }
            else -> {
                return when (item.itemId) {
                    R.id.test_toolBar_menu_add,
                    R.id.test_toolBar_menu_edit,
                    R.id.test_toolBar_menu_delete,
                    R.id.test_toolBar_menu_sort -> {
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    private fun refreshView() {
        refreshContentView(recyclerViewAdapter.dataList)
        recyclerViewAdapter.clearSelection()
    }

    private fun handelItem(itemState: ItemState, data: MainDataContent) {
        val view = View.inflate(this, R.layout.dialog_test_item_edit, null)
        val problemEditText = view.editText_test_item_edit_problem
        val answerEditText = view.editText_test_item_edit_answer

        problemEditText.setText(data.problem)
        answerEditText.setText(data.answer)

        problemEditText.requestFocus()
        KeyboardManager.show(this)

        val title = when (itemState) {
            ItemState.ADD -> R.string.test_item_edit_dialog_title_add
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

            cancelDialog(view, dialog)

            if (itemState == ItemState.ADD) {
                handleItemAdd(data)
            }
        }

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            cancelDialog(view, dialog)
        }
    }

    private fun handleItemAdd(data: MainDataContent) {
        val itemArr = ItemAddPosition.getDescriptionArr(this)
        val builder = AlertDialog.Builder(this)

        val dialog = builder
            .setTitle(R.string.app_item_add_position_title)
            .setItems(itemArr) { _, which ->
                when (ItemAddPosition.get(which)) {
                    ItemAddPosition.FIRST -> {
                        recyclerViewAdapter.addItemAtFirst(data)
                    }
                    ItemAddPosition.LAST -> {
                        recyclerViewAdapter.addItemAtLast(data)
                    }
                }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RequestCode.TEST_SEARCH.get() -> {
                val searchData = MainDataManager.searchContentData ?: return
                recyclerViewAdapter.select(searchData)
            }
            RequestCode.TEST_SORT.get() -> {
                recyclerViewAdapter.notifyDataSetChanged()
                recyclerViewAdapter.scrollToPosition(0)
            }
        }
    }

}