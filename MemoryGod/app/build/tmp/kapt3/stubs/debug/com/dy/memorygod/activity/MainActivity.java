package com.dy.memorygod.activity;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u008c\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\b\r\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0014\u001a\u00020\u0015H\u0002J\b\u0010\u0016\u001a\u00020\u0015H\u0002J\b\u0010\u0017\u001a\u00020\u0015H\u0002J\b\u0010\u0018\u001a\u00020\u0015H\u0002J\b\u0010\u0019\u001a\u00020\u0015H\u0002J\b\u0010\u001a\u001a\u00020\u0015H\u0002J\u0016\u0010\u001b\u001a\u00020\u00152\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dH\u0002J\u0016\u0010\u001f\u001a\u00020\u00152\f\u0010 \u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dH\u0002J\b\u0010!\u001a\u00020\u0015H\u0002J\b\u0010\"\u001a\u00020\u0015H\u0002J\u0016\u0010#\u001a\u00020\u00152\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dH\u0002J\b\u0010%\u001a\u00020\u0015H\u0002J\"\u0010&\u001a\u00020\u00152\u0006\u0010\'\u001a\u00020(2\u0006\u0010)\u001a\u00020(2\b\u0010*\u001a\u0004\u0018\u00010+H\u0014J\b\u0010,\u001a\u00020\u0015H\u0016J\u0012\u0010-\u001a\u00020\u00152\b\u0010.\u001a\u0004\u0018\u00010/H\u0014J\u0010\u00100\u001a\u0002012\u0006\u00102\u001a\u000203H\u0016J\b\u00104\u001a\u00020\u0015H\u0014J\u0010\u00105\u001a\u00020\u00152\u0006\u00106\u001a\u00020(H\u0016J\u0010\u00107\u001a\u00020\u00152\u0006\u00108\u001a\u00020(H\u0016J\u0010\u00109\u001a\u0002012\u0006\u0010:\u001a\u00020;H\u0016J\b\u0010<\u001a\u00020\u0015H\u0014J\u0016\u0010=\u001a\u00020\u00152\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0>H\u0002J\u0016\u0010?\u001a\u00020\u00152\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0>H\u0002J\u0010\u0010@\u001a\u00020\u00152\u0006\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010A\u001a\u00020\u0015H\u0002J\b\u0010B\u001a\u00020\u0015H\u0002J\b\u0010C\u001a\u00020\u0015H\u0002J\b\u0010D\u001a\u00020\u0015H\u0002J\b\u0010E\u001a\u00020\u0015H\u0002J\b\u0010F\u001a\u00020\u0015H\u0002J\b\u0010G\u001a\u00020\u0015H\u0002J\b\u0010H\u001a\u00020\u0015H\u0002J\b\u0010I\u001a\u00020\u0015H\u0002J\u0018\u0010J\u001a\u00020\u00152\u0006\u0010*\u001a\u00020\u001e2\u0006\u0010K\u001a\u00020LH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082D\u00a2\u0006\u0002\n\u0000\u00a8\u0006M"}, d2 = {"Lcom/dy/memorygod/activity/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Lcom/dy/memorygod/adapter/MainRecyclerViewEventListener;", "()V", "emptyTextView", "Landroid/widget/TextView;", "fileLoadPermissionListener", "Lcom/gun0912/tedpermission/PermissionListener;", "fileSavePermissionListener", "firebaseAnalytics", "Lcom/google/firebase/analytics/FirebaseAnalytics;", "mode", "Lcom/dy/memorygod/enums/ActivityModeMain;", "phoneNumberPermissionListener", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "recyclerViewAdapter", "Lcom/dy/memorygod/adapter/MainRecyclerViewAdapter;", "threadDelay", "", "checkFileLoadPermission", "", "checkFileSavePermission", "checkPhoneNumberPermission", "clearMode", "finishWithBackup", "handleFileLoad", "handleFileLoadData", "dataList", "", "Lcom/dy/memorygod/data/MainData;", "handleFileLoadDataPosition", "checkedDataList", "handleFileSave", "handleItemAdd", "handleItemDelete", "selectedDataList", "loadBackupData", "onActivityResult", "requestCode", "", "resultCode", "data", "Landroid/content/Intent;", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateOptionsMenu", "", "menu", "Landroid/view/Menu;", "onDestroy", "onItemClicked", "position", "onItemSelected", "size", "onOptionsItemSelected", "item", "Landroid/view/MenuItem;", "onStart", "refreshContentView", "", "refreshContentViewVisibility", "refreshMode", "refreshView", "saveBackupData", "setAD", "setDefaultData", "setMode", "setPhoneNumberData", "setRecyclerView", "setSampleData", "setToolbar", "startTest", "activityMode", "Lcom/dy/memorygod/enums/ActivityModeTest;", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity implements com.dy.memorygod.adapter.MainRecyclerViewEventListener {
    private final com.dy.memorygod.adapter.MainRecyclerViewAdapter recyclerViewAdapter = null;
    private com.dy.memorygod.enums.ActivityModeMain mode = com.dy.memorygod.enums.ActivityModeMain.NORMAL;
    private android.widget.TextView emptyTextView;
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    private final long threadDelay = 100L;
    private final com.google.firebase.analytics.FirebaseAnalytics firebaseAnalytics = null;
    private final com.gun0912.tedpermission.PermissionListener phoneNumberPermissionListener = null;
    private final com.gun0912.tedpermission.PermissionListener fileSavePermissionListener = null;
    private final com.gun0912.tedpermission.PermissionListener fileLoadPermissionListener = null;
    private java.util.HashMap _$_findViewCache;
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    protected void onStart() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    private final void setToolbar() {
    }
    
    private final void setAD() {
    }
    
    private final void setDefaultData() {
    }
    
    private final void setPhoneNumberData() {
    }
    
    private final void setSampleData() {
    }
    
    private final void loadBackupData() {
    }
    
    private final void saveBackupData() {
    }
    
    private final void setRecyclerView() {
    }
    
    private final void refreshContentView(java.util.List<com.dy.memorygod.data.MainData> dataList) {
    }
    
    private final void refreshContentViewVisibility(java.util.List<com.dy.memorygod.data.MainData> dataList) {
    }
    
    private final void refreshMode(com.dy.memorygod.enums.ActivityModeMain mode) {
    }
    
    private final void clearMode() {
    }
    
    private final void setMode() {
    }
    
    @java.lang.Override()
    public void onItemClicked(int position) {
    }
    
    private final void startTest(com.dy.memorygod.data.MainData data, com.dy.memorygod.enums.ActivityModeTest activityMode) {
    }
    
    private final void checkPhoneNumberPermission() {
    }
    
    @java.lang.Override()
    public void onItemSelected(int size) {
    }
    
    @java.lang.Override()
    public void onBackPressed() {
    }
    
    private final void finishWithBackup() {
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
    
    private final void handleItemAdd() {
    }
    
    private final void handleItemDelete(java.util.List<com.dy.memorygod.data.MainData> selectedDataList) {
    }
    
    private final void refreshView() {
    }
    
    @java.lang.Override()
    protected void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    private final void checkFileSavePermission() {
    }
    
    private final void handleFileSave() {
    }
    
    private final void checkFileLoadPermission() {
    }
    
    private final void handleFileLoad() {
    }
    
    private final void handleFileLoadData(java.util.List<com.dy.memorygod.data.MainData> dataList) {
    }
    
    private final void handleFileLoadDataPosition(java.util.List<com.dy.memorygod.data.MainData> checkedDataList) {
    }
    
    public MainActivity() {
        super();
    }
}