package com.dy.memorygod.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000b\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0003234B\u0015\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u000e\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\nJ\u000e\u0010\u0017\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\nJ\u0006\u0010\u0018\u001a\u00020\u0015J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\u0010\u0010\u001b\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\u001aH\u0016J\f\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\n0\u001eJ\u0006\u0010\u001f\u001a\u00020\u001aJ\u000e\u0010 \u001a\u00020\u00152\u0006\u0010\u000f\u001a\u00020\u0010J\u001c\u0010!\u001a\u00020\u00152\n\u0010\"\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u001c\u001a\u00020\u001aH\u0016J\u001c\u0010#\u001a\u00060\u0002R\u00020\u00002\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020\u001aH\u0016J\u0014\u0010\'\u001a\u00020\u00152\f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tJ\u0010\u0010(\u001a\u00020\u00152\u0006\u0010)\u001a\u00020*H\u0002J\u0010\u0010+\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\nH\u0002J\u0010\u0010,\u001a\u00020\u00152\u0006\u0010)\u001a\u00020*H\u0002J\u000e\u0010-\u001a\u00020\u00152\u0006\u0010.\u001a\u00020\u001aJ\u000e\u0010/\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\nJ\u0006\u00100\u001a\u00020\u0015J\b\u00101\u001a\u00020\u0015H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\f\"\u0004\b\r\u0010\u000eR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00130\u0012X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u00065"}, d2 = {"Lcom/dy/memorygod/adapter/MainRecyclerViewAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/dy/memorygod/adapter/MainRecyclerViewAdapter$ViewHolder;", "context", "Landroid/content/Context;", "onEventListener", "Lcom/dy/memorygod/adapter/MainRecyclerViewEventListener;", "(Landroid/content/Context;Lcom/dy/memorygod/adapter/MainRecyclerViewEventListener;)V", "dataList", "", "Lcom/dy/memorygod/data/MainData;", "getDataList", "()Ljava/util/List;", "setDataList", "(Ljava/util/List;)V", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "tracker", "Landroidx/recyclerview/selection/SelectionTracker;", "", "addItemAtFirst", "", "data", "addItemAtLast", "clearSelection", "getItemCount", "", "getItemId", "position", "getSelectedList", "", "getSelectionSize", "init", "onBindViewHolder", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "refresh", "refreshBgColor", "itemView", "Landroid/view/View;", "refreshSelection", "refreshVisibility", "scrollToPosition", "idx", "select", "selectAll", "setTracker", "MyItemDetailsLookup", "MyItemKeyProvider", "ViewHolder", "app_release"})
public final class MainRecyclerViewAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.dy.memorygod.adapter.MainRecyclerViewAdapter.ViewHolder> {
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    private androidx.recyclerview.selection.SelectionTracker<java.lang.Long> tracker;
    @org.jetbrains.annotations.NotNull()
    public java.util.List<com.dy.memorygod.data.MainData> dataList;
    private final android.content.Context context = null;
    private final com.dy.memorygod.adapter.MainRecyclerViewEventListener onEventListener = null;
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dy.memorygod.data.MainData> getDataList() {
        return null;
    }
    
    public final void setDataList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dy.memorygod.data.MainData> p0) {
    }
    
    public final void init(@org.jetbrains.annotations.NotNull()
    androidx.recyclerview.widget.RecyclerView recyclerView) {
    }
    
    private final void setTracker() {
    }
    
    @java.lang.Override()
    public long getItemId(int position) {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.dy.memorygod.adapter.MainRecyclerViewAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    private final void refreshVisibility(android.view.View itemView) {
    }
    
    private final void refreshBgColor(android.view.View itemView) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.dy.memorygod.adapter.MainRecyclerViewAdapter.ViewHolder holder, int position) {
    }
    
    public final void refresh(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dy.memorygod.data.MainData> dataList) {
    }
    
    public final void addItemAtFirst(@org.jetbrains.annotations.NotNull()
    com.dy.memorygod.data.MainData data) {
    }
    
    public final void addItemAtLast(@org.jetbrains.annotations.NotNull()
    com.dy.memorygod.data.MainData data) {
    }
    
    private final void refreshSelection(com.dy.memorygod.data.MainData data) {
    }
    
    public final int getSelectionSize() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dy.memorygod.data.MainData> getSelectedList() {
        return null;
    }
    
    public final void select(@org.jetbrains.annotations.NotNull()
    com.dy.memorygod.data.MainData data) {
    }
    
    public final void scrollToPosition(int idx) {
    }
    
    public final void selectAll() {
    }
    
    public final void clearSelection() {
    }
    
    public MainRecyclerViewAdapter(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    com.dy.memorygod.adapter.MainRecyclerViewEventListener onEventListener) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\b\u0086\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0017\u0010\u0006\u001a\u0004\u0018\u00010\u00022\u0006\u0010\u0007\u001a\u00020\bH\u0016\u00a2\u0006\u0002\u0010\tJ\u0010\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u0002H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/dy/memorygod/adapter/MainRecyclerViewAdapter$MyItemKeyProvider;", "Landroidx/recyclerview/selection/ItemKeyProvider;", "", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "(Lcom/dy/memorygod/adapter/MainRecyclerViewAdapter;Landroidx/recyclerview/widget/RecyclerView;)V", "getKey", "position", "", "(I)Ljava/lang/Long;", "getPosition", "key", "app_release"})
    public final class MyItemKeyProvider extends androidx.recyclerview.selection.ItemKeyProvider<java.lang.Long> {
        private final androidx.recyclerview.widget.RecyclerView recyclerView = null;
        
        @org.jetbrains.annotations.Nullable()
        @java.lang.Override()
        public java.lang.Long getKey(int position) {
            return null;
        }
        
        @java.lang.Override()
        public int getPosition(long key) {
            return 0;
        }
        
        public MyItemKeyProvider(@org.jetbrains.annotations.NotNull()
        androidx.recyclerview.widget.RecyclerView recyclerView) {
            super(0);
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u0018\u0010\u0006\u001a\n\u0012\u0004\u0012\u00020\u0002\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\tH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/dy/memorygod/adapter/MainRecyclerViewAdapter$MyItemDetailsLookup;", "Landroidx/recyclerview/selection/ItemDetailsLookup;", "", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "(Lcom/dy/memorygod/adapter/MainRecyclerViewAdapter;Landroidx/recyclerview/widget/RecyclerView;)V", "getItemDetails", "Landroidx/recyclerview/selection/ItemDetailsLookup$ItemDetails;", "event", "Landroid/view/MotionEvent;", "app_release"})
    public final class MyItemDetailsLookup extends androidx.recyclerview.selection.ItemDetailsLookup<java.lang.Long> {
        private final androidx.recyclerview.widget.RecyclerView recyclerView = null;
        
        @org.jetbrains.annotations.Nullable()
        @java.lang.Override()
        public androidx.recyclerview.selection.ItemDetailsLookup.ItemDetails<java.lang.Long> getItemDetails(@org.jetbrains.annotations.NotNull()
        android.view.MotionEvent event) {
            return null;
        }
        
        public MyItemDetailsLookup(@org.jetbrains.annotations.NotNull()
        androidx.recyclerview.widget.RecyclerView recyclerView) {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/dy/memorygod/adapter/MainRecyclerViewAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/dy/memorygod/adapter/MainRecyclerViewAdapter;Landroid/view/View;)V", "titleTextView", "Landroid/widget/TextView;", "bind", "", "data", "Lcom/dy/memorygod/data/MainData;", "isActivated", "", "getItemDetails", "Landroidx/recyclerview/selection/ItemDetailsLookup$ItemDetails;", "", "app_release"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        private final android.widget.TextView titleTextView = null;
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.dy.memorygod.data.MainData data, boolean isActivated) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final androidx.recyclerview.selection.ItemDetailsLookup.ItemDetails<java.lang.Long> getItemDetails() {
            return null;
        }
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
    }
}