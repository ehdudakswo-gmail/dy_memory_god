package com.dy.memorygod.enums

enum class FirestoreLogType {

    MAIN_ITEM_CLICK,
    MAIN_DATA_EXCEL_SAVE_ERROR,
    MAIN_DATA_EXCEL_LOAD_ERROR;

    fun get() = toString()

}