package com.dy.memorygod.adapter

import androidx.recyclerview.widget.RecyclerView

interface MainSortRecyclerViewEventListener {
    fun onDragStarted(viewHolder: RecyclerView.ViewHolder)
}