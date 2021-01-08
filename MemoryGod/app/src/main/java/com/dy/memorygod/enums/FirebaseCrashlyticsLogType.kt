package com.dy.memorygod.enums

enum class FirebaseCrashlyticsLogType {

    MAIN_ITEM_CLICK,

    BACKUP_DATA_LOAD_ERROR,
    SAMPLE_DATA_LOAD_ERROR,
    MAIN_DATA_DB_LOAD_ERROR,
    MAIN_DATA_DB_SAVE_ERROR,
    MAIN_DATA_EXCEL_SAVE_ERROR,
    MAIN_DATA_EXCEL_LOAD_ERROR;

    fun get() = toString()

}