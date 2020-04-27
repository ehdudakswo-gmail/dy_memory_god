package com.dy.memorygod.manager;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\u0014\u001a\u00020\u0015J\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\n0\u00062\u0006\u0010\u0014\u001a\u00020\u0015R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0017\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/dy/memorygod/manager/ContactManager;", "", "()V", "ERROR_CONTACT_CURSOR", "", "ERROR_CONTACT_EMAIL", "", "Lcom/dy/memorygod/data/ContactEmailData;", "ERROR_CONTACT_EMPTY", "ERROR_CONTACT_PHONE_NUMBER", "Lcom/dy/memorygod/data/ContactPhoneNumberData;", "getERROR_CONTACT_PHONE_NUMBER", "()Ljava/util/List;", "ERROR_MESSAGE", "getERROR_MESSAGE", "()Ljava/lang/String;", "setERROR_MESSAGE", "(Ljava/lang/String;)V", "PHONE_NUMBER_SEPARATOR", "getEmailList", "context", "Landroid/content/Context;", "getPhoneNumberList", "app_debug"})
public final class ContactManager {
    private static final java.lang.String ERROR_CONTACT_CURSOR = "ERROR_CONTACT_CURSOR";
    private static final java.lang.String ERROR_CONTACT_EMPTY = "ERROR_CONTACT_EMPTY";
    private static final java.lang.String PHONE_NUMBER_SEPARATOR = "-";
    @org.jetbrains.annotations.NotNull()
    private static java.lang.String ERROR_MESSAGE = "ERROR_MESSAGE";
    @org.jetbrains.annotations.NotNull()
    private static final java.util.List<com.dy.memorygod.data.ContactPhoneNumberData> ERROR_CONTACT_PHONE_NUMBER = null;
    private static final java.util.List<com.dy.memorygod.data.ContactEmailData> ERROR_CONTACT_EMAIL = null;
    public static final com.dy.memorygod.manager.ContactManager INSTANCE = null;
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getERROR_MESSAGE() {
        return null;
    }
    
    public final void setERROR_MESSAGE(@org.jetbrains.annotations.NotNull()
    java.lang.String p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dy.memorygod.data.ContactPhoneNumberData> getERROR_CONTACT_PHONE_NUMBER() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dy.memorygod.data.ContactPhoneNumberData> getPhoneNumberList(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.dy.memorygod.data.ContactEmailData> getEmailList(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    private ContactManager() {
        super();
    }
}