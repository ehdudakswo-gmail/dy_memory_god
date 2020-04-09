package com.dy.memorygod.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dy.memorygod.R
import com.dy.memorygod.data.MainData
import kotlinx.android.synthetic.main.item_main.view.*

class MainSearchRecyclerViewAdapter(
    private val context: Context,
    private val onEventListener: MainSearchRecyclerViewEventListener
) :
    RecyclerView.Adapter<MainSearchRecyclerViewAdapter.ViewHolder>() {

    lateinit var dataList: MutableList<MainData>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_main, parent, false)

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
            itemView.textView_main_item_title

        fun bind(data: MainData) {
            titleTextView.text = data.title

            refreshVisibility(itemView)
        }
    }

    private fun refreshVisibility(itemView: View) {
        val reorderImageView: ImageView =
            itemView.imageView_main_item_reorder

        reorderImageView.visibility = View.GONE
    }

    fun refresh(dataList: MutableList<MainData>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

}