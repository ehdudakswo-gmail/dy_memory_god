package com.dy.memorygod.data

data class FirestoreConfig(
    var isAllEnable: Boolean = true,
    var isLogEnable: Boolean = true,
    var stopLogTypes: List<*>? = null
) {

    override fun toString(): String {
        return "{isAllEnable=$isAllEnable, isLogEnable=$isLogEnable, stopLogTypes=$stopLogTypes}"
    }

}