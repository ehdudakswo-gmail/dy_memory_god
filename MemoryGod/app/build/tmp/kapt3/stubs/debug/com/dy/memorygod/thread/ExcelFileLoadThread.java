package com.dy.memorygod.thread;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0016\u001a\u00020\u0017H\u0016R \u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001c\u0010\f\u001a\u0004\u0018\u00010\rX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0018"}, d2 = {"Lcom/dy/memorygod/thread/ExcelFileLoadThread;", "Ljava/lang/Thread;", "file", "Ljava/io/File;", "(Ljava/io/File;)V", "dataList", "", "Lcom/dy/memorygod/data/MainData;", "getDataList", "()Ljava/util/List;", "setDataList", "(Ljava/util/List;)V", "exception", "", "getException", "()Ljava/lang/String;", "setException", "(Ljava/lang/String;)V", "inputStream", "Ljava/io/FileInputStream;", "workbook", "Lorg/apache/poi/hssf/usermodel/HSSFWorkbook;", "run", "", "app_debug"})
public final class ExcelFileLoadThread extends java.lang.Thread {
    @org.jetbrains.annotations.Nullable()
    private java.lang.String exception;
    @org.jetbrains.annotations.NotNull()
    public java.util.List<com.dy.memorygod.data.MainData> dataList;
    private org.apache.poi.hssf.usermodel.HSSFWorkbook workbook;
    private java.io.FileInputStream inputStream;
    private final java.io.File file = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getException() {
        return null;
    }
    
    public final void setException(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dy.memorygod.data.MainData> getDataList() {
        return null;
    }
    
    public final void setDataList(@org.jetbrains.annotations.NotNull()
    java.util.List<com.dy.memorygod.data.MainData> p0) {
    }
    
    @java.lang.Override()
    public void run() {
    }
    
    public ExcelFileLoadThread(@org.jetbrains.annotations.NotNull()
    java.io.File file) {
        super();
    }
}