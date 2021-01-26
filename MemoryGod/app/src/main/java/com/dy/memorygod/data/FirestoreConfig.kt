package com.dy.memorygod.data

data class FirestoreConfig(
    var isLogEnable: Boolean = true,
    var isShareDataDownload: Boolean = true,
    var stopLogTypes: List<*>? = null
)