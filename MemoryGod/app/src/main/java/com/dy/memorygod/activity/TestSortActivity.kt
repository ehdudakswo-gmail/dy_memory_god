package com.dy.memorygod.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dy.memorygod.R
import com.dy.memorygod.adapter.TestSortRecyclerViewAdapter
import com.dy.memorygod.adapter.TestSortRecyclerViewEventListener
import com.dy.memorygod.adapter.TestSortRecyclerViewTouchHelperCallback
import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.manager.JsonManager
import com.dy.memorygod.manager.MainDataManager
import kotlinx.android.synthetic.main.activity_test_sort.*

class TestSortActivity : AppCompatActivity(), TestSortRecyclerViewEventListener {

    private val selectedMainData = MainDataManager.selectedData
    private lateinit var originData: MainData

    private val recyclerViewAdapter = TestSortRecyclerViewAdapter(this, this)
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_sort)

        setToolbar()
        setRecyclerView()
        setItemTouchHelper()
        setOriginData()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_test_sort)
        val actionBar = supportActionBar!!

        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)

        val titleFormat = getString(R.string.test_sort_toolBar_title)
        val title = String.format(titleFormat, selectedMainData.title)
        textView_test_sort_toolbar_title.text = title
    }

    private fun setRecyclerView() {
        recyclerView = recyclerView_test_sort
        val layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        refreshContentView(selectedMainData.contentList)
    }

    private fun refreshContentView(dataList: MutableList<MainDataContent>) {
        recyclerViewAdapter.refresh(dataList)
        refreshContentViewVisibility(dataList)
    }

    private fun refreshContentViewVisibility(dataList: MutableList<MainDataContent>) {
        val emptyTextView = textView_test_sort_item_empty
        val recyclerView = recyclerView_test_sort

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

    private fun setItemTouchHelper() {
        val callBack = TestSortRecyclerViewTouchHelperCallback(recyclerViewAdapter)
        itemTouchHelper = ItemTouchHelper(callBack)
        itemTouchHelper.attachToRecyclerView(recyclerView_test_sort)
    }

    private fun setOriginData() {
        val json = JsonManager.toJson(selectedMainData)
        originData = JsonManager.toMainData(json)
    }

    override fun onDragStarted(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_test_sort, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val dataList = recyclerViewAdapter.dataList

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            R.id.test_sort_toolBar_menu_name -> {
                dataList.sortBy { it.problem }
            }
            R.id.test_sort_toolBar_menu_name_reverse -> {
                dataList.sortByDescending { it.problem }
            }
            R.id.test_sort_toolBar_menu_test_check -> {
                dataList.sortBy { it.testCheck }
            }
            R.id.test_sort_toolBar_menu_test_check_reverse -> {
                dataList.sortByDescending { it.testCheck }
            }
        }

        recyclerViewAdapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(0)

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (isSorted()) {
            handleSortedResult()
        } else {
            super.onBackPressed()
        }
    }

    private fun isSorted(): Boolean {
        val currentDataList = recyclerViewAdapter.dataList
        val originDataList = originData.contentList

        val currentSize = currentDataList.size
        val originSize = originDataList.size

        if (currentSize != originSize) {
            return true
        }

        for (idx in 0 until currentSize) {
            val currentData = currentDataList[idx]
            val originData = originDataList[idx]

            if (currentData != originData) {
                return true
            }
        }

        return false
    }

    private fun handleSortedResult() {
        val builder = AlertDialog.Builder(this)
        val dialog = builder
            .setMessage(R.string.app_item_sort_check_message)
            .setPositiveButton(R.string.app_dialog_yes, null)
            .setNegativeButton(R.string.app_dialog_no, null)
            .setNeutralButton(R.string.app_dialog_close, null)
            .show()

        dialog.setCanceledOnTouchOutside(false)
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
            restoreOriginData()
            dialog.dismiss()
            finish()
        }

        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun restoreOriginData() {
        val currentDataList = recyclerViewAdapter.dataList
        val originDataList = originData.contentList

        currentDataList.clear()
        for (originData in originDataList) {
            currentDataList.add(originData)
        }
    }

}