package com.dy.memorygod.enums

enum class RequestCode {

    TEST_SEARCH,
    TEST_SORT;

    fun get() = ordinal

}