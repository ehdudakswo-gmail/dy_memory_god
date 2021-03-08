package com.dy.memorygod.manager

import android.content.Context
import android.os.Bundle
import com.dy.memorygod.enums.LogType
import com.google.firebase.analytics.FirebaseAnalytics

object FirebaseAnalyticsManager {

    fun log(context: Context, type: LogType, message: String) {
        val name = type.get()
        val bundle = Bundle()
        bundle.putString("message", message)

        FirebaseAnalytics
            .getInstance(context)
            .logEvent(name, bundle)

        val logData = FirebaseLogManager.getLogData(type, message)
        AndroidLogManager.d("FirebaseAnalyticsManager log--record : $logData")
    }

}