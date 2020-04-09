package com.dy.memorygod.adapter

import androidx.recyclerview.widget.RecyclerView

interface MainSortRecyclerViewTouchHelperListener {
    fun onItemMove(from: Int, to: Int): Boolean
    fun onItemSwipe(position: Int)
    fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int)
}