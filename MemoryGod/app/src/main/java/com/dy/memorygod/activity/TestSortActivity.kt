package com.dy.memorygod.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dy.memorygod.R
import com.dy.memorygod.adapter.TestSortRecyclerViewAdapter
import com.dy.memorygod.adapter.TestSortRecyclerViewEventListener
import com.dy.memorygod.adapter.TestSortRecyclerViewTouchHelperCallback
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.manager.MainDataManager
import kotlinx.android.synthetic.main.activity_test_sort.*

class TestSortActivity : AppCompatActivity(), TestSortRecyclerViewEventListener {

    private val selectedData = MainDataManager.selectedData
    private val recyclerViewAdapter = TestSortRecyclerViewAdapter(this, this)

    private lateinit var emptyTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_sort)

        setToolbar()
        setRecyclerView()
        setItemTouchHelper()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_test_sort)
        val actionBar = supportActionBar!!

        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)

        val titleFormat = getString(R.string.app_sort_toolBar_title)
        val titleDescription = MainDataManager.selectedData.title
        val title = String.format(titleFormat, titleDescription)
        textView_test_sort_toolbar_title.text = title
    }

    private fun setRecyclerView() {
        emptyTextView = textView_test_sort_item_empty
        recyclerView = recyclerView_test_sort

        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        refreshContentView(selectedData.contentList)
        recyclerViewAdapter.initIdx()
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

    private fun setItemTouchHelper() {
        val callBack = TestSortRecyclerViewTouchHelperCallback(recyclerViewAdapter)
        itemTouchHelper = ItemTouchHelper(callBack)
        itemTouchHelper.attachToRecyclerView(recyclerView_test_sort)
    }

    override fun onDragStarted(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_test_sort, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
            else -> {
                recyclerViewAdapter.sort(item.itemId)
                recyclerViewAdapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(0)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (recyclerViewAdapter.isOrderChange()) {
            handleResult()
        } else {
            super.onBackPressed()
        }
    }

    private fun handleResult() {
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
            recyclerViewAdapter.restoreOrder()
            dialog.dismiss()
            finish()
        }

        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
            dialog.dismiss()
        }
    }

}