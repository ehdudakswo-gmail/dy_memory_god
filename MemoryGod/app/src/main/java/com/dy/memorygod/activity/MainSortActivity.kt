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
import com.dy.memorygod.adapter.MainSortRecyclerViewAdapter
import com.dy.memorygod.adapter.MainSortRecyclerViewEventListener
import com.dy.memorygod.adapter.MainSortRecyclerViewTouchHelperCallback
import com.dy.memorygod.data.MainData
import com.dy.memorygod.manager.MainDataManager
import kotlinx.android.synthetic.main.activity_main_sort.*

class MainSortActivity : AppCompatActivity(), MainSortRecyclerViewEventListener {

    private val selectedData = MainDataManager.dataList
    private val recyclerViewAdapter = MainSortRecyclerViewAdapter(this, this)

    private lateinit var emptyTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemTouchHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_sort)

        setToolbar()
        setRecyclerView()
        setItemTouchHelper()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_main_sort)
        val actionBar = supportActionBar!!

        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)

        val titleFormat = getString(R.string.app_sort_toolBar_title)
        val titleDescription = getString(R.string.app_name)
        val title = String.format(titleFormat, titleDescription)
        textView_main_sort_toolbar_title.text = title
    }

    private fun setRecyclerView() {
        emptyTextView = textView_main_sort_item_empty
        recyclerView = recyclerView_main_sort

        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        refreshContentView(selectedData)
        recyclerViewAdapter.initIdx()
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

    private fun setItemTouchHelper() {
        val callBack = MainSortRecyclerViewTouchHelperCallback(recyclerViewAdapter)
        itemTouchHelper = ItemTouchHelper(callBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onDragStarted(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main_sort, menu)

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