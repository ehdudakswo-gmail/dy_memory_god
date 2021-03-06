package com.dy.memorygod.manager

import android.content.Context
import com.dy.memorygod.GlobalApplication
import com.dy.memorygod.enums.LogType
import java.text.SimpleDateFormat
import java.util.*

object FirebaseLogManager {

    private const val DATA_JOIN_SEPARATOR = ", "
    private const val DATA_JOIN_OPEN = "{"
    private const val DATA_JOIN_CLOSE = "}"
    const val DATA_EMPTY = "-"

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
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateFormat.format(Date())

        val lastErrorArr = arrayOf(date, logMessage)
        val lastError = getJoinData(lastErrorArr)
        GlobalApplication.instance.firestoreLastError = lastError
    }

    fun getJoinData(arr: Array<String>): String {
        val joinData = arr.joinToString(DATA_JOIN_SEPARATOR)

        return "$DATA_JOIN_OPEN$joinData$DATA_JOIN_CLOSE"
    }

    fun getLogInfo(type: LogType, message: String): String {
        val arr = arrayOf(type.get(), message)

        return getJoinData(arr)
    }

}