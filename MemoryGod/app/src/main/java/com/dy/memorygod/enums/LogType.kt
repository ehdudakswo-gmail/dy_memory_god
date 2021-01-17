package com.dy.memorygod.enums

enum class LogType {

    MAIN_ITEM_CLICK,
    HOME_UPDATE_OK_CLICK,
    HOME_UPDATE_CANCEL_CLICK,

    BACKUP_DATA_LOAD_ERROR,
    SAMPLE_DATA_LOAD_ERROR,
    MAIN_DATA_DB_LOAD_ERROR,
    MAIN_DATA_DB_SAVE_ERROR,
    MAIN_DATA_EXCEL_SAVE_ERROR,
    MAIN_DATA_EXCEL_LOAD_ERROR,
    PHONE_NUMBER_LOAD_ERROR,
    PHONE_NUMBER_REFRESH_ERROR,
    ADMOB_FAILED_TO_LOAD,

    MAIN_DATA_EXCEL_SAVE_SUCCESS,
    MAIN_DATA_EXCEL_LOAD_SUCCESS,

    FIRESTORE_ERROR;

    fun get() = toString()

}