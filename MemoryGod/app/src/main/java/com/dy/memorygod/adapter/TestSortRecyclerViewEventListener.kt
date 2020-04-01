package com.dy.memorygod.adapter

import androidx.recyclerview.widget.RecyclerView

interface TestSortRecyclerViewEventListener {
    fun onDragStarted(viewHolder: RecyclerView.ViewHolder)
}