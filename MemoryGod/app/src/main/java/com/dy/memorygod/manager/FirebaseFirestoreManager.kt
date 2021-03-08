package com.dy.memorygod.manager

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.dy.memorygod.GlobalApplication
import com.dy.memorygod.enums.LogType
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


object FirebaseFirestoreManager {
    // config
    const val CONFIG = "config"
    const val CONFIG_ANDROID = "android"

    // log
    private const val LOG = "log"

    fun log(context: Context, type: LogType, message: String) {
        if (isLogStop(type)) {
            val logInfo = FirebaseLogManager.getLogInfo(type, message)
            AndroidLogManager.d("FirebaseFirestoreManager log--stop : $logInfo")
            return
        }

        // collection
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection(LOG)
        val nowDate = Date()

        // document
        val documentPathFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val documentPath = documentPathFormat.format(nowDate)
        val document = collection.document(documentPath)

        /** 필수 **/
        val documentData = getDocumentData(nowDate)
        document.set(documentData)

        // collection2
        val collection2PathFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val collection2Path = collection2PathFormat.format(nowDate)
        val collection2 = document.collection(collection2Path)

        // create document data
        val data = getLogData(context, type, message)
        collection2
            .add(data)
            .addOnSuccessListener {
                val logInfo = FirebaseLogManager.getLogInfo(type, message)
                AndroidLogManager.d("FirebaseFirestoreManager log--record : $logInfo")
            }
            .addOnFailureListener { exception ->
                val logMessage =
                    "FirebaseFirestoreManager log addOnFailureListener exception : $exception"
                FirebaseLogManager.logFirestoreError(context, logMessage)
            }
    }

    private fun isLogStop(type: LogType): Boolean {
        val appConfig = GlobalApplication.instance.firestoreConfig
        if (!appConfig.logEnable) {
            return true
        }

        val logStopTypes = appConfig.logStopTypes
        val typeStr = type.get()

        if (logStopTypes != null && logStopTypes.contains(typeStr)) {
            return true
        }

        return false
    }

    private fun getDocumentData(date: Date): HashMap<String, Any> {
        return hashMapOf(
            "lastUpdate" to date
        )
    }

    private fun getLogData(
        context: Context,
        type: LogType,
        message: String
    ): HashMap<String, Any> {

        return hashMapOf(
            "date" to Date(),
            "type" to type,
            "message" to message,
            "deviceID" to getDeviceId(context),
            "deviceModel" to getDeviceModel(),
            "deviceOS" to getDeviceOs(),
            "country" to getCountry(),
            "appVersion" to getAppVersion()
        )
    }

    private fun getDeviceId(context: Context): String {
        return try {
            Settings
                .Secure
                .getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (ex: Exception) {
            ex.toString()
        }
    }

    private fun getDeviceModel(): String {
        return try {
            Build.MODEL
        } catch (ex: Exception) {
            ex.toString()
        }
    }

    private fun getDeviceOs(): String {
        return try {
            Build.VERSION.RELEASE
        } catch (ex: Exception) {
            ex.toString()
        }
    }

    private fun getCountry(): String {
        return try {
            Locale.getDefault().country
        } catch (ex: Exception) {
            ex.toString()
        }
    }

    private fun getAppVersion(): String {
        return GlobalApplication.instance.appVersion
    }

}