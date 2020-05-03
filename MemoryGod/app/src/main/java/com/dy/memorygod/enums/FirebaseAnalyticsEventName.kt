package com.dy.memorygod.enums

enum class FirebaseAnalyticsEventName {

    MAIN_DATA_DB_LOAD_ERROR,
    MAIN_DATA_DB_SAVE_ERROR,
    MAIN_DATA_EXCEL_LOAD_ERROR,
    MAIN_DATA_EXCEL_SAVE_ERROR,
    PHONE_NUMBER_LOAD_ERROR,
    PHONE_NUMBER_LOAD_ERROR_EXCEL_SAVE,
    AD_FAILED_TO_LOAD;

    fun get() = toString()

}