package com.dy.memorygod.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dy.memorygod.R
import com.dy.memorygod.data.MainDataContent

class TestRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<TestRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView =
            itemView.findViewById(R.id.textView_test_recyclerView_title)

        fun bind(data: MainDataContent) {
            titleTextView.text = data.title
        }
    }

    interface ItemClickListener {
        fun onClick()
    }

    private var dataList: List<MainDataContent> = ArrayList()
    private lateinit var itemClickListener: TestRecyclerViewAdapter.ItemClickListener
    lateinit var selectedItem: MainDataContent

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_test_recycler_view, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        holder.bind(data)
        holder.itemView.setOnClickListener {
            selectedItem = data
            itemClickListener.onClick()
        }
    }

    fun setItemClickListener(itemClickListener: TestRecyclerViewAdapter.ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun refresh(dataList: List<MainDataContent>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

}