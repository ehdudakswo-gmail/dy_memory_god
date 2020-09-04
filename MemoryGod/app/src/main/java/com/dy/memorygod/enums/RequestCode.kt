package com.dy.memorygod.enums

enum class RequestCode {

    MAIN_SORT,
    MAIN_SEARCH,
    MAIN_FILE_OPEN,
    MAIN_FILE_SAVE,

    TEST_SORT,
    TEST_SEARCH;

    fun get() = ordinal

}