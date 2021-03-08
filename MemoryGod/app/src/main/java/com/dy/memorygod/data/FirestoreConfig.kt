package com.dy.memorygod.data

data class FirestoreConfig(
    var logEnable: Boolean = true,
    var logStopTypes: List<*>? = null,
    var shareDataEnable: Boolean = true
)