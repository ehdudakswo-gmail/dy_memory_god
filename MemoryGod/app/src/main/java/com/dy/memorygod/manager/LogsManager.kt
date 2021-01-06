package com.dy.memorygod.manager

import android.util.Log
import com.dy.memorygod.GlobalApplication
import com.dy.memorygod.enums.AppMode

object LogsManager {

    private const val TAG = "ehdudakswo"

    fun d(message: String) {
        if (GlobalApplication.instance.appMode == AppMode.DEBUG) {
            Log.d(TAG, message)
        }
    }

}