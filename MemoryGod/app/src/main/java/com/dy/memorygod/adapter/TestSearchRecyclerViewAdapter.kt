package com.dy.memorygod.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dy.memorygod.R
import com.dy.memorygod.data.MainDataContent
import kotlinx.android.synthetic.main.item_test.view.*

class TestSearchRecyclerViewAdapter(
    private val context: Context,
    private val onEventListener: TestSearchRecyclerViewEventListener
) :
    RecyclerView.Adapter<TestSearchRecyclerViewAdapter.ViewHolder>() {

    lateinit var dataList: MutableList<MainDataContent>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_test, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = holder.itemView
        val data = dataList[position]

        holder.bind(data)
        itemView.setOnClickListener {
            onEventListener.onItemClicked(position)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView =
            itemView.textView_test_item_title

        fun bind(data: MainDataContent) {
            titleTextView.text = data.problem

            refreshVisibility(itemView)
            refreshTestCheckColor(itemView, data)
        }
    }

    private fun refreshVisibility(itemView: View) {
        val reorderImageView: ImageView =
            itemView.imageView_test_item_reorder

        reorderImageView.visibility = View.GONE
    }

    private fun refreshTestCheckColor(itemView: View, data: MainDataContent) {
        val testCheckView: View =
            itemView.view_test_item_test_check

        val color = data.testCheck.color
        testCheckView.setBackgroundResource(color)
    }

    fun refresh(dataList: MutableList<MainDataContent>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

}