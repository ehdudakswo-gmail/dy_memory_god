package com.dy.memorygod.manager

import com.dy.memorygod.enums.LogType
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * Firebase Crashlytics 이벤트 유형 = "심각하지 않음"
 */

object FirebaseCrashlyticsManager {

    private const val NEW_LINE = "\n"
    private const val ITEM_SEPARATOR = " / "

    fun record(type: LogType, message: String) {
        val arr = arrayOf(
            "type : $type",
            "message : $message"
        )

        val exceptionMessage = getExceptionMessage(arr)
        val exception = Exception(exceptionMessage)
        record(exception)

        LogsManager.d("FirebaseCrashlyticsManager exception : $exception")
    }

    private fun getExceptionMessage(exceptionMessageArray: Array<String>): String {
        val newLineArr = exceptionMessageArray.map { NEW_LINE + it }
        return newLineArr.joinToString(ITEM_SEPARATOR)
    }

    private fun record(exception: Exception) {
        FirebaseCrashlytics
            .getInstance()
            .recordException(exception)
    }

}