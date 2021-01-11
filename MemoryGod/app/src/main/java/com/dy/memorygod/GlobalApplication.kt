package com.dy.memorygod

import android.app.Application
import com.dy.memorygod.data.FirestoreConfig
import com.dy.memorygod.enums.AppMode

class GlobalApplication : Application() {

    companion object {
        lateinit var instance: GlobalApplication
            private set
    }

    val appMode = AppMode.RELEASE
    var appVersion = "null"
    val firestoreConfig = FirestoreConfig()
    var firestoreLastError: String? = null

    override fun onCreate() {
        super.onCreate()

        instance = this
        appVersion = getAppVersionName()
    }

    private fun getAppVersionName(): String {
        return try {
            val packageName = packageName
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName
        } catch (ex: Exception) {
            ex.toString()
        }
    }

    fun getAppInfo(): String {
        val info = arrayOf(
            "appVersion : $appVersion",
            "firestoreConfig : $firestoreConfig",
            "firestoreLastError : $firestoreLastError"
        )

        return info.map { "- " + it }.joinToString("\n")
    }

}