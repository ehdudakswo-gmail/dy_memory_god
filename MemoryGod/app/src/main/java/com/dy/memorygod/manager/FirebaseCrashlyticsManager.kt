package com.dy.memorygod.manager

import com.dy.memorygod.enums.LogType
import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * Firebase Crashlytics 이벤트 유형 = "심각하지 않음"
 */

object FirebaseCrashlyticsManager {

    fun log(type: LogType, message: String) {
        val arr = arrayOf(
            "type : $type",
            "message : $message"
        )

        val exceptionMessage = FirebaseLogManager.getJoinData(arr)
        val exception = Exception(exceptionMessage)

        FirebaseCrashlytics
            .getInstance()
            .recordException(exception)

        val logInfo = FirebaseLogManager.getLogInfo(type, message)
        AndroidLogManager.d("FirebaseCrashlyticsManager log--record : $logInfo")
    }

}