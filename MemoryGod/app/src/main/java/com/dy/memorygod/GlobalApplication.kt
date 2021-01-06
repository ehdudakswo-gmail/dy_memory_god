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
    val firestoreConfig = FirestoreConfig()

    override fun onCreate() {
        super.onCreate()

        instance = this
    }

}