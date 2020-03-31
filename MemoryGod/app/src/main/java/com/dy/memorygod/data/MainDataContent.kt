package com.dy.memorygod.data

import com.dy.memorygod.enums.TestCheck

data class MainDataContent(
    var problem: String,
    var answer: String,
    var testCheck: TestCheck = TestCheck.NONE
)