package com.dy.memorygod.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dy.memorygod.R
import com.dy.memorygod.data.MainData

class MainRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView =
            itemView.findViewById(R.id.textView_main_recyclerView_subject)

        fun bind(data: MainData) {
            textView.text = data.subject
        }
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