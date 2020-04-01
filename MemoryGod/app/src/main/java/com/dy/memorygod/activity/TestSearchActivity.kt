package com.dy.memorygod.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dy.memorygod.R
import com.dy.memorygod.adapter.TestSearchRecyclerViewAdapter
import com.dy.memorygod.adapter.TestSearchRecyclerViewEventListener
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.manager.KeyboardManager
import com.dy.memorygod.manager.MainDataManager
import kotlinx.android.synthetic.main.activity_test_search.*
import java.util.*

class TestSearchActivity : AppCompatActivity(), TestSearchRecyclerViewEventListener {

    private val selectedMainData = MainDataManager.selectedData
    private val recyclerViewAdapter = TestSearchRecyclerViewAdapter(this, this)
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_search)

        setToolbar()
        setRecyclerView()
        setOnClickListener()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_test_search)
        val actionBar = supportActionBar!!

        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun setRecyclerView() {
        val recyclerView = recyclerView_test_search
        val layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        recyclerView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_UP -> {
                    setInitState(recyclerView)
                }
            }

            false
        }

        refreshContentView(selectedMainData.contentList)
    }

    private fun setInitState(view: View) {
        KeyboardManager.hide(this, view)
    }

    private fun refreshContentView(dataList: MutableList<MainDataContent>) {
        recyclerViewAdapter.refresh(dataList)
        refreshContentViewVisibility(dataList)
    }

    private fun refreshContentViewVisibility(dataList: MutableList<MainDataContent>) {
        val emptyTextView = textView_test_search_item_empty
        val recyclerView = recyclerView_test_search

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
        frameLayout_test_search_content.setOnClickListener {
            setInitState(frameLayout_test_search_content)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_test_search, menu)
        searchView = menu.findItem(R.id.test_search_toolBar_menu_search).actionView as SearchView

        val hintFormat = getString(R.string.test_search_toolBar_menu_search_hint)
        val hint = String.format(hintFormat, selectedMainData.title)

        searchView.queryHint = hint
        searchView.onActionViewExpanded()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val text = newText!!.trim()
                val lowerText = text.toLowerCase(Locale.ROOT)

                val filterList =
                    selectedMainData.contentList.filter {
                        val lowerData = it.problem.toLowerCase(Locale.ROOT)
                        lowerData.contains(lowerText)
                    }.toMutableList()

                recyclerViewAdapter.refresh(filterList)
                refreshContentView(filterList)

                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val query = searchView.query.trim()
        if (query.isNotEmpty()) {
            searchView.setQuery("", false)
            return
        }

        super.onBackPressed()
    }

    override fun onItemClicked(position: Int) {
        val data = recyclerViewAdapter.dataList[position]
        MainDataManager.searchContentData = data

        setResult(RESULT_OK)
        finish()
    }

}