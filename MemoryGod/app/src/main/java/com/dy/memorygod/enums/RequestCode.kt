package com.dy.memorygod.enums

enum class RequestCode {

    MAIN_SORT,
    MAIN_SEARCH,

    TEST_SORT,
    TEST_SEARCH;

    fun get() = ordinal

}