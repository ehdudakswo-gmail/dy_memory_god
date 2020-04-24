package com.dy.memorygod.enums

enum class CrashlyticsKey {

    MAIN_DATA_DB_LOAD_ERROR,
    MAIN_DATA_DB_SAVE_ERROR,
    MAIN_DATA_EXCEL_LOAD_ERROR,
    MAIN_DATA_EXCEL_SAVE_ERROR,
    AD_FAILED_TO_LOAD;

    fun get() = toString()

}