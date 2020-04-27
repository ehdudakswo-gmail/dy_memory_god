package com.dy.memorygod.entity;

import java.lang.System;

@androidx.room.Entity(tableName = "main_data_table")
@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b&\b\u0007\u0018\u00002\u00020\u0001B_\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\u0010\b\u001a\u0004\u0018\u00010\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\b\b\u0002\u0010\u000e\u001a\u00020\u000f\u0012\b\b\u0002\u0010\u0010\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u0011\u001a\u00020\u0007\u0012\b\b\u0002\u0010\u0012\u001a\u00020\u0013\u00a2\u0006\u0002\u0010\u0014R\u001e\u0010\u0011\u001a\u00020\u00078\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0016\"\u0004\b\u0017\u0010\u0018R\u001e\u0010\n\u001a\u00020\u000b8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u001e\u0010\f\u001a\u00020\r8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u001e\u0010\u000e\u001a\u00020\u000f8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\"\"\u0004\b#\u0010$R\u001e\u0010\u0002\u001a\u00020\u00038\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b%\u0010&\"\u0004\b\'\u0010(R\u001e\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b)\u0010*\"\u0004\b+\u0010,R\u001e\u0010\u0010\u001a\u00020\u00078\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b-\u0010\u0016\"\u0004\b.\u0010\u0018R\u001e\u0010\u0012\u001a\u00020\u00138\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b/\u00100\"\u0004\b1\u00102R\u001e\u0010\u0006\u001a\u00020\u00078\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u0016\"\u0004\b4\u0010\u0018R \u0010\b\u001a\u0004\u0018\u00010\t8\u0006@\u0006X\u0087\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b5\u00106\"\u0004\b7\u00108\u00a8\u00069"}, d2 = {"Lcom/dy/memorygod/entity/MainDataEntity;", "", "id", "", "idx", "", "title", "", "updatedDate", "Ljava/util/Date;", "dataType", "Lcom/dy/memorygod/enums/DataType;", "dataTypePhone", "Lcom/dy/memorygod/enums/DataTypePhone;", "hasContent", "", "problem", "answer", "testCheck", "Lcom/dy/memorygod/enums/TestCheck;", "(JILjava/lang/String;Ljava/util/Date;Lcom/dy/memorygod/enums/DataType;Lcom/dy/memorygod/enums/DataTypePhone;ZLjava/lang/String;Ljava/lang/String;Lcom/dy/memorygod/enums/TestCheck;)V", "getAnswer", "()Ljava/lang/String;", "setAnswer", "(Ljava/lang/String;)V", "getDataType", "()Lcom/dy/memorygod/enums/DataType;", "setDataType", "(Lcom/dy/memorygod/enums/DataType;)V", "getDataTypePhone", "()Lcom/dy/memorygod/enums/DataTypePhone;", "setDataTypePhone", "(Lcom/dy/memorygod/enums/DataTypePhone;)V", "getHasContent", "()Z", "setHasContent", "(Z)V", "getId", "()J", "setId", "(J)V", "getIdx", "()I", "setIdx", "(I)V", "getProblem", "setProblem", "getTestCheck", "()Lcom/dy/memorygod/enums/TestCheck;", "setTestCheck", "(Lcom/dy/memorygod/enums/TestCheck;)V", "getTitle", "setTitle", "getUpdatedDate", "()Ljava/util/Date;", "setUpdatedDate", "(Ljava/util/Date;)V", "app_release"})
public final class MainDataEntity {
    @androidx.room.PrimaryKey()
    private long id;
    @androidx.room.ColumnInfo(name = "idx")
    private int idx;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "title")
    private java.lang.String title;
    @org.jetbrains.annotations.Nullable()
    @androidx.room.ColumnInfo(name = "updatedDate")
    private java.util.Date updatedDate;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "dataType")
    private com.dy.memorygod.enums.DataType dataType;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "dataTypePhone")
    private com.dy.memorygod.enums.DataTypePhone dataTypePhone;
    @androidx.room.ColumnInfo(name = "hasContent")
    private boolean hasContent;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "problem")
    private java.lang.String problem;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "answer")
    private java.lang.String answer;
    @org.jetbrains.annotations.NotNull()
    @androidx.room.ColumnInfo(name = "testCheck")
    private com.dy.memorygod.enums.TestCheck testCheck;
    
    public final long getId() {
        return 0L;
    }
    
    public final void setId(long p0) {
    }
    
    public final int getIdx() {
        return 0;
    }
    
    public final void setIdx(int p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getTitle() {
        return null;
    }
    
    public final void setTitle(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.Date getUpdatedDate() {
        return null;
    }
    
    public final void setUpdatedDate(@org.jetbrains.annotations.Nullable()
    java.util.Date p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dy.memorygod.enums.DataType getDataType() {
        return null;
    }
    
    public final void setDataType(@org.jetbrains.annotations.NotNull()
    com.dy.memorygod.enums.DataType p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dy.memorygod.enums.DataTypePhone getDataTypePhone() {
        return null;
    }
    
    public final void setDataTypePhone(@org.jetbrains.annotations.NotNull()
    com.dy.memorygod.enums.DataTypePhone p0) {
    }
    
    public final boolean getHasContent() {
        return false;
    }
    
    public final void setHasContent(boolean p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getProblem() {
        return null;
    }
    
    public final void setProblem(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAnswer() {
        return null;
    }
    
    public final void setAnswer(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.dy.memorygod.enums.TestCheck getTestCheck() {
        return null;
    }
    
    public final void setTestCheck(@org.jetbrains.annotations.NotNull()
    com.dy.memorygod.enums.TestCheck p0) {
    }
    
    public MainDataEntity(long id, int idx, @org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.Nullable()
    java.util.Date updatedDate, @org.jetbrains.annotations.NotNull()
    com.dy.memorygod.enums.DataType dataType, @org.jetbrains.annotations.NotNull()
    com.dy.memorygod.enums.DataTypePhone dataTypePhone, boolean hasContent, @org.jetbrains.annotations.NotNull()
    java.lang.String problem, @org.jetbrains.annotations.NotNull()
    java.lang.String answer, @org.jetbrains.annotations.NotNull()
    com.dy.memorygod.enums.TestCheck testCheck) {
        super();
    }
}