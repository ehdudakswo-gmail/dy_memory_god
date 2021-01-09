package com.dy.memorygod.manager

import android.content.Context
import com.dy.memorygod.GlobalApplication
import com.dy.memorygod.enums.LogType
import java.util.*

object FirebaseLogManager {

    private const val DATA_JOIN_SEPARATOR = ", "
    private const val DATA_JOIN_OPEN = "{"
    private const val DATA_JOIN_CLOSE = "}"

    fun log(context: Context, logType: LogType, logMessage: String) {
        // by log record time
        FirebaseFirestoreManager.log(context, logType, logMessage)
        FirebaseCrashlyticsManager.log(logType, logMessage)
        FirebaseAnalyticsManager.log(context, logType, logMessage)
    }

    fun logFirestoreError(context: Context, logMessage: String) {
        // except firestore log
        val logType = LogType.FIRESTORE_ERROR
        FirebaseCrashlyticsManager.log(logType, logMessage)
        FirebaseAnalyticsManager.log(context, logType, logMessage)

        // app config
        val lastErrorArr = arrayOf(Date().toString(), logMessage)
        val lastError = getJoinData(lastErrorArr)
        GlobalApplication.instance.firestoreLastError = lastError
    }

    fun getJoinData(arr: Array<String>): String {
        val joinData = arr.joinToString(DATA_JOIN_SEPARATOR)

        return "$DATA_JOIN_OPEN$joinData$DATA_JOIN_CLOSE"
    }

    fun getLogData(type: LogType, message: String): String {
        val arr = arrayOf(type.get(), message)

        return getJoinData(arr)
    }

}