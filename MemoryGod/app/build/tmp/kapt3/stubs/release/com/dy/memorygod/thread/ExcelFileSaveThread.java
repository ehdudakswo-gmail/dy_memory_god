package com.dy.memorygod.thread;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bJ\u0018\u0010\u001a\u001a\u00020\n2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u001b\u001a\u00020\u0006H\u0002J\b\u0010\u001c\u001a\u00020\u001dH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\f\u001a\u0004\u0018\u00010\nX\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\bX\u0086.\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2 = {"Lcom/dy/memorygod/thread/ExcelFileSaveThread;", "Ljava/lang/Thread;", "context", "Landroid/content/Context;", "dataList", "", "Lcom/dy/memorygod/data/MainData;", "filePath", "Ljava/io/File;", "fileName", "", "(Landroid/content/Context;Ljava/util/List;Ljava/io/File;Ljava/lang/String;)V", "exception", "getException", "()Ljava/lang/String;", "setException", "(Ljava/lang/String;)V", "file", "getFile", "()Ljava/io/File;", "setFile", "(Ljava/io/File;)V", "outputStream", "Ljava/io/FileOutputStream;", "workbook", "Lorg/apache/poi/hssf/usermodel/HSSFWorkbook;", "getTitle", "data", "run", "", "app_release"})
public final class ExcelFileSaveThread extends java.lang.Thread {
    @org.jetbrains.annotations.Nullable()
    private java.lang.String exception;
    @org.jetbrains.annotations.NotNull()
    public java.io.File file;
    private org.apache.poi.hssf.usermodel.HSSFWorkbook workbook;
    private java.io.FileOutputStream outputStream;
    private final android.content.Context context = null;
    private final java.util.List<com.dy.memorygod.data.MainData> dataList = null;
    private final java.io.File filePath = null;
    private final java.lang.String fileName = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getException() {
        return null;
    }
    
    public final void setException(@org.jetbrains.annotations.Nullable()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.io.File getFile() {
        return null;
    }
    
    public final void setFile(@org.jetbrains.annotations.NotNull()
    java.io.File p0) {
    }
    
    @java.lang.Override()
    public void run() {
    }
    
    private final java.lang.String getTitle(android.content.Context context, com.dy.memorygod.data.MainData data) {
        return null;
    }
    
    public ExcelFileSaveThread(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    java.util.List<com.dy.memorygod.data.MainData> dataList, @org.jetbrains.annotations.NotNull()
    java.io.File filePath, @org.jetbrains.annotations.NotNull()
    java.lang.String fileName) {
        super();
    }
}