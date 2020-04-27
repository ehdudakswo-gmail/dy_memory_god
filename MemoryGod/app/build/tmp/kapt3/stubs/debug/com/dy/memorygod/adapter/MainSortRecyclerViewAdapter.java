package com.dy.memorygod.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u00012\u00020\u0003:\u0001-B\u0015\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u0006\u0010\u0012\u001a\u00020\u0013J\u0006\u0010\u0014\u001a\u00020\u0015J\u001c\u0010\u0016\u001a\u00020\u00132\n\u0010\u0017\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0018\u001a\u00020\u0011H\u0016J\u001c\u0010\u0019\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0011H\u0016J\u0018\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\u0011H\u0016J\u0010\u0010 \u001a\u00020\u00132\u0006\u0010\u0018\u001a\u00020\u0011H\u0016J\u001a\u0010!\u001a\u00020\u00132\b\u0010\"\u001a\u0004\u0018\u00010#2\u0006\u0010$\u001a\u00020\u0011H\u0016J\u0014\u0010%\u001a\u00020\u00132\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nJ\u0010\u0010&\u001a\u00020\u00132\u0006\u0010\'\u001a\u00020(H\u0002J\u0010\u0010)\u001a\u00020\u00132\u0006\u0010\'\u001a\u00020(H\u0002J\u0006\u0010*\u001a\u00020\u0013J\u000e\u0010+\u001a\u00020\u00132\u0006\u0010,\u001a\u00020\u0011R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\nX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006."}, d2 = {"Lcom/dy/memorygod/adapter/MainSortRecyclerViewAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/dy/memorygod/adapter/MainSortRecyclerViewAdapter$ViewHolder;", "Lcom/dy/memorygod/adapter/MainSortRecyclerViewTouchHelperListener;", "context", "Landroid/content/Context;", "onEventListener", "Lcom/dy/memorygod/adapter/MainSortRecyclerViewEventListener;", "(Landroid/content/Context;Lcom/dy/memorygod/adapter/MainSortRecyclerViewEventListener;)V", "dataList", "", "Lcom/dy/memorygod/data/MainData;", "getDataList", "()Ljava/util/List;", "setDataList", "(Ljava/util/List;)V", "getItemCount", "", "initIdx", "", "isOrderChange", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "onItemMove", "from", "to", "onItemSwipe", "onSelectedChanged", "viewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "actionState", "refresh", "refreshBgColor", "itemView", "Landroid/view/View;", "refreshVisibility", "restoreOrder", "sort", "itemId", "ViewHolder", "app_debug"})
public final class MainSortRecyclerViewAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.dy.memorygod.adapter.MainSortRecyclerViewAdapter.ViewHolder> implements com.dy.memorygod.adapter.MainSortRecyclerViewTouchHelperListener {
    @org.jetbrains.annotations.NotNull()
    public java.util.List<com.dy.memorygod.data.MainData> dataList;
    private final android.content.Context context = null;
    private final com.dy.memorygod.adapter.MainSortRecyclerViewEventListener onEventListener = null;
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dy.memorygod.data.MainData> getDataList() {
        return null;
    }
    
    public final void setDataList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dy.memorygod.data.MainData> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.dy.memorygod.adapter.MainSortRecyclerViewAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.dy.memorygod.adapter.MainSortRecyclerViewAdapter.ViewHolder holder, int position) {
    }
    
    private final void refreshVisibility(android.view.View itemView) {
    }
    
    private final void refreshBgColor(android.view.View itemView) {
    }
    
    @java.lang.Override()
    public boolean onItemMove(int from, int to) {
        return false;
    }
    
    @java.lang.Override()
    public void onItemSwipe(int position) {
    }
    
    @java.lang.Override()
    public void onSelectedChanged(@org.jetbrains.annotations.Nullable()
    androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, int actionState) {
    }
    
    public final void refresh(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dy.memorygod.data.MainData> dataList) {
    }
    
    public final void initIdx() {
    }
    
    public final boolean isOrderChange() {
        return false;
    }
    
    public final void restoreOrder() {
    }
    
    public final void sort(int itemId) {
    }
    
    public MainSortRecyclerViewAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.dy.memorygod.adapter.MainSortRecyclerViewEventListener onEventListener) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/dy/memorygod/adapter/MainSortRecyclerViewAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/dy/memorygod/adapter/MainSortRecyclerViewAdapter;Landroid/view/View;)V", "titleTextView", "Landroid/widget/TextView;", "bind", "", "data", "Lcom/dy/memorygod/data/MainData;", "app_debug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        private final android.widget.TextView titleTextView = null;
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.dy.memorygod.data.MainData data) {
        }
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
    }
}