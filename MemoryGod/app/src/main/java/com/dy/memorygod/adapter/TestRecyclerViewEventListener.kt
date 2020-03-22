package com.dy.memorygod.adapter

import androidx.recyclerview.widget.RecyclerView

interface TestRecyclerViewEventListener {
    fun onItemClicked(position: Int)
    fun onItemLongClicked(position: Int)
    fun onItemDeleted(position: Int)
    fun onDragStarted(viewHolder: RecyclerView.ViewHolder)
}