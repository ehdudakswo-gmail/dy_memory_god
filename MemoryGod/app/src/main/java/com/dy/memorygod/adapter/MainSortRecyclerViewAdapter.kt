package com.dy.memorygod.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dy.memorygod.R
import com.dy.memorygod.data.MainData
import kotlinx.android.synthetic.main.item_main.view.*

class MainSortRecyclerViewAdapter(
    private val context: Context,
    private val onEventListener: MainSortRecyclerViewEventListener
) :
    RecyclerView.Adapter<MainSortRecyclerViewAdapter.ViewHolder>(),
    MainSortRecyclerViewTouchHelperListener {

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
        itemView.imageView_main_item_reorder.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    onEventListener.onDragStarted(holder)

                    val view = itemView.cardView_main_recyclerView
                    view.setBackgroundResource(R.color.color_item_bg_reorder)
                }
            }

            false
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView =
            itemView.textView_main_item_title

        fun bind(data: MainData) {
            titleTextView.text = data.title

            refreshVisibility(itemView)
            refreshBgColor(itemView)
        }
    }

    private fun refreshVisibility(itemView: View) {
        val reorderImageView: ImageView =
            itemView.imageView_main_item_reorder

        reorderImageView.visibility = View.VISIBLE
    }

    private fun refreshBgColor(itemView: View) {
        val cardView: View =
            itemView.cardView_main_recyclerView

        cardView.setBackgroundResource(R.color.color_item_bg_normal)
    }

    override fun onItemMove(from: Int, to: Int): Boolean {
        val fromItem = dataList[from]
        dataList.removeAt(from)
        dataList.add(to, fromItem)

        notifyItemMoved(from, to)
        return true
    }

    override fun onItemSwipe(position: Int) {
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        when (actionState) {
            ItemTouchHelper.ACTION_STATE_IDLE -> {
                notifyDataSetChanged()
            }
        }
    }

    fun refresh(dataList: MutableList<MainData>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    fun initIdx() {
        for (i in 0 until dataList.size) {
            val data = dataList[i]
            data.idx = i
        }
    }

    fun isOrderChange(): Boolean {
        for (i in 0 until dataList.size) {
            val data = dataList[i]
            if (data.idx != i) {
                return true
            }
        }

        return false
    }

    fun restoreOrder() {
        dataList.sortBy { it.idx }
    }

    fun sort(itemId: Int) {
        when (itemId) {
            R.id.main_sort_toolBar_menu_name -> {
                dataList.sortBy { it.title }
            }
            R.id.main_sort_toolBar_menu_name_reverse -> {
                dataList.sortByDescending { it.title }
            }
        }
    }

}