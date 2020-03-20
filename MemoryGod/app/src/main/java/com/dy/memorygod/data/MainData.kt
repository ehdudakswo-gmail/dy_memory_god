package com.dy.memorygod.data

data class MainData(
    val subject: String,
    val contentList: List<MainDataContent>,
    val isReadOnly: Boolean = true
)