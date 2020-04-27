package com.dy.memorygod.activity;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\b\u0010\u0010\u001a\u00020\u000fH\u0016J\u0012\u0010\u0011\u001a\u00020\u000f2\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0014J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\u000f2\u0006\u0010\u0019\u001a\u00020\u001aH\u0016J\u0010\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u001dH\u0016J\u0016\u0010\u001e\u001a\u00020\u000f2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 H\u0002J\u0016\u0010\"\u001a\u00020\u000f2\f\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020!0 H\u0002J\b\u0010#\u001a\u00020\u000fH\u0002J\b\u0010$\u001a\u00020\u000fH\u0002J\b\u0010%\u001a\u00020\u000fH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006&"}, d2 = {"Lcom/dy/memorygod/activity/TestSortActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/dy/memorygod/adapter/TestSortRecyclerViewEventListener;", "()V", "emptyTextView", "Landroid/widget/TextView;", "itemTouchHelper", "Landroidx/recyclerview/widget/ItemTouchHelper;", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "recyclerViewAdapter", "Lcom/dy/memorygod/adapter/TestSortRecyclerViewAdapter;", "selectedData", "Lcom/dy/memorygod/data/MainData;", "handleResult", "", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "", "menu", "Landroid/view/Menu;", "onDragStarted", "viewHolder", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "refreshContentView", "dataList", "", "Lcom/dy/memorygod/data/MainDataContent;", "refreshContentViewVisibility", "setItemTouchHelper", "setRecyclerView", "setToolbar", "app_debug"})
public final class TestSortActivity extends androidx.appcompat.app.AppCompatActivity implements com.dy.memorygod.adapter.TestSortRecyclerViewEventListener {
    private final com.dy.memorygod.data.MainData selectedData = null;
    private final com.dy.memorygod.adapter.TestSortRecyclerViewAdapter recyclerViewAdapter = null;
    private android.widget.TextView emptyTextView;
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    private androidx.recyclerview.widget.ItemTouchHelper itemTouchHelper;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setToolbar() {
    }
    
    private final void setRecyclerView() {
    }
    
    private final void refreshContentView(java.util.List<com.dy.memorygod.data.MainDataContent> dataList) {
    }
    
    private final void refreshContentViewVisibility(java.util.List<com.dy.memorygod.data.MainDataContent> dataList) {
    }
    
    private final void setItemTouchHelper() {
    }
    
    @java.lang.Override()
    public void onDragStarted(@org.jetbrains.annotations.NotNull()
    androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder) {
    }
    
    @java.lang.Override()
    public boolean onCreateOptionsMenu(@org.jetbrains.annotations.NotNull()
    android.view.Menu menu) {
        return false;
    }
    
    @java.lang.Override()
    public boolean onOptionsItemSelected(@org.jetbrains.annotations.NotNull()
    android.view.MenuItem item) {
        return false;
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    private final void handleResult() {
    }
    
    public TestSortActivity() {
        super();
    }
}