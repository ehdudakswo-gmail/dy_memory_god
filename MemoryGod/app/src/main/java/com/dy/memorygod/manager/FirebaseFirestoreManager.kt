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
    // db
    val db = FirebaseFirestore.getInstance()

    // logs
    const val LOGS = "logs"

    // config
    const val CONFIG = "config"
    const val CONFIG_FIRESTORE = "firestore"
    const val CONFIG_FIRESTORE_isAllEnable = "isAllEnable"
    const val CONFIG_FIRESTORE_isLogEnable = "isLogEnable"
    const val CONFIG_FIRESTORE_stopLogTypes = "stopLogTypes"

    fun log(context: Context, type: LogType, message: String) {
        if (isLogStop(type)) {
            val logData = FirebaseLogManager.getLogData(type, message)
            LogsManager.d("FirebaseFirestoreManager log--stop : $logData")
            return
        }

        val nowDate = Date()
        val documentDateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val document = documentDateFormat.format(nowDate)

        val collectionDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val collection = collectionDateFormat.format(nowDate)

        val data = getLogData(context, type, message)

        db.collection(LOGS)
            .document(document)
            .collection(collection)
            .add(data)
            .addOnSuccessListener { documentReference ->
                val logData = FirebaseLogManager.getLogData(type, message)
                LogsManager.d("FirebaseFirestoreManager log--record : $logData")
                LogsManager.d("FirebaseFirestoreManager log addOnSuccessListener documentReference.id : ${documentReference.id}")
            }
            .addOnFailureListener { exception ->
                val logMessage =
                    "FirebaseFirestoreManager log addOnFailureListener exception : $exception"
                LogsManager.d(logMessage)
                FirebaseLogManager.logFirestoreError(context, logMessage)
            }
    }

    private fun isLogStop(type: LogType): Boolean {
        val appConfig = GlobalApplication.instance.firestoreConfig
        if (!appConfig.isAllEnable) {
            return true
        }

        if (!appConfig.isLogEnable) {
            return true
        }

        val stopLogTypes = appConfig.stopLogTypes
        val typeStr = type.get()

        if (stopLogTypes != null && stopLogTypes.contains(typeStr)) {
            return true
        }

        return false
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