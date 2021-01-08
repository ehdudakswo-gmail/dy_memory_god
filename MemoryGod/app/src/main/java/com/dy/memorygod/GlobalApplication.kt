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
            "firestoreConfig-isAllEnable : ${firestoreConfig.isAllEnable}",
            "firestoreConfig-isLogEnable : ${firestoreConfig.isLogEnable}"
        )

        return info.joinToString("\n")
    }

}