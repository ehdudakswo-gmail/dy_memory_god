package com.dy.memorygod.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H&J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0005H&J\u001a\u0010\n\u001a\u00020\b2\b\u0010\u000b\u001a\u0004\u0018\u00010\f2\u0006\u0010\r\u001a\u00020\u0005H&\u00a8\u0006\u000e"}, d2 = {"Lcom/dy/memorygod/adapter/MainSortRecyclerViewTouchHelperListener;", "", "onItemMove", "", "from", "", "to", "onItemSwipe", "", "position", "onSelectedChanged", "viewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "actionState", "app_release"})
public abstract interface MainSortRecyclerViewTouchHelperListener {
    
    public abstract boolean onItemMove(int from, int to);
    
    public abstract void onItemSwipe(int position);
    
    public abstract void onSelectedChanged(@org.jetbrains.annotations.Nullable()
    androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, int actionState);
}