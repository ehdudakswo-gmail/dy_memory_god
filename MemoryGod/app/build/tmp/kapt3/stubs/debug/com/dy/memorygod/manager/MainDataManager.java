package com.dy.memorygod.manager;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u001d\u001a\u00020\u001eJ\u0014\u0010\u001f\u001a\u00020\u001e2\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004J\u0006\u0010\r\u001a\u00020\u001eR \u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\f\"\u0004\b\r\u0010\u000eR\u001c\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001c\u0010\u0015\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001a\u001a\u00020\u0005X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0017\"\u0004\b\u001c\u0010\u0019\u00a8\u0006 "}, d2 = {"Lcom/dy/memorygod/manager/MainDataManager;", "", "()V", "dataList", "", "Lcom/dy/memorygod/data/MainData;", "getDataList", "()Ljava/util/List;", "setDataList", "(Ljava/util/List;)V", "isLoadingComplete", "", "()Z", "setLoadingComplete", "(Z)V", "searchContentData", "Lcom/dy/memorygod/data/MainDataContent;", "getSearchContentData", "()Lcom/dy/memorygod/data/MainDataContent;", "setSearchContentData", "(Lcom/dy/memorygod/data/MainDataContent;)V", "searchData", "getSearchData", "()Lcom/dy/memorygod/data/MainData;", "setSearchData", "(Lcom/dy/memorygod/data/MainData;)V", "selectedData", "getSelectedData", "setSelectedData", "init", "", "refresh", "app_debug"})
public final class MainDataManager {
    private static boolean isLoadingComplete = false;
    @org.jetbrains.annotations.NotNull()
    public static java.util.List<com.dy.memorygod.data.MainData> dataList;
    @org.jetbrains.annotations.NotNull()
    public static com.dy.memorygod.data.MainData selectedData;
    @org.jetbrains.annotations.Nullable()
    private static com.dy.memorygod.data.MainData searchData;
    @org.jetbrains.annotations.Nullable()
    private static com.dy.memorygod.data.MainDataContent searchContentData;
    public static final com.dy.memorygod.manager.MainDataManager INSTANCE = null;
    
    public final boolean isLoadingComplete() {
        return false;
    }
    
    public final void setLoadingComplete(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dy.memorygod.data.MainData> getDataList() {
        return null;
    }
    
    public final void setDataList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dy.memorygod.data.MainData> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dy.memorygod.data.MainData getSelectedData() {
        return null;
    }
    
    public final void setSelectedData(@org.jetbrains.annotations.NotNull()
    com.dy.memorygod.data.MainData p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dy.memorygod.data.MainData getSearchData() {
        return null;
    }
    
    public final void setSearchData(@org.jetbrains.annotations.Nullable()
    com.dy.memorygod.data.MainData p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.dy.memorygod.data.MainDataContent getSearchContentData() {
        return null;
    }
    
    public final void setSearchContentData(@org.jetbrains.annotations.Nullable()
    com.dy.memorygod.data.MainDataContent p0) {
    }
    
    public final void init() {
    }
    
    public final void refresh(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dy.memorygod.data.MainData> dataList) {
    }
    
    public final void setLoadingComplete() {
    }
    
    private MainDataManager() {
        super();
    }
}