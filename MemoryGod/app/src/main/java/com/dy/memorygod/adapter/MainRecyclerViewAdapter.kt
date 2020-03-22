package com.dy.memorygod.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dy.memorygod.R
import com.dy.memorygod.data.MainData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView =
            itemView.findViewById(R.id.textView_main_recyclerView_title)

        fun bind(data: MainData) {
            titleTextView.text = getTitleText(data)
        }
    }

    private fun getTitleText(data: MainData): String {
        val title = data.title
        val updatedDate = data.updatedDate ?: return title

        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dateFormat = simpleDateFormat.format(updatedDate)

        return "$title ($dateFormat)"
    }

    interface ItemClickListener {
        fun onClick()
    }

    private var dataList: List<MainData> = ArrayList()
    private lateinit var itemClickListener: ItemClickListener
    lateinit var selectedItem: MainData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_main_recycler_view, parent, false)

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

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    fun refresh(dataList: List<MainData>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

}