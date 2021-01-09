package com.dy.memorygod.manager

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.dy.memorygod.GlobalApplication
import com.dy.memorygod.enums.LogType
import com.google.firebase.firestore.FirebaseFirestore
import java.net.Inet4Address
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*


object FirebaseFirestoreManager {

    private val db = FirebaseFirestore.getInstance()
    private const val COLLECTION_LOGS = "logs"
    private const val COLLECTION_LOGS_DOCUMENT_DATE = "date"

    const val COLLECTION_CONFIG = "config"
    const val COLLECTION_CONFIG_DOCUMENT_FIRESTORE = "firestore"
    const val COLLECTION_CONFIG_DOCUMENT_FIRESTORE_FIELD_isAllEnable = "isAllEnable"
    const val COLLECTION_CONFIG_DOCUMENT_FIRESTORE_FIELD_isLogEnable = "isLogEnable"
    const val COLLECTION_CONFIG_DOCUMENT_FIRESTORE_FIELD_stopLogTypes = "stopLogTypes"

    fun log(context: Context, type: LogType, message: String) {
        if (isLogStop(type)) {
            val logData = FirebaseLogManager.getLogData(type, message)
            LogsManager.d("FirebaseFirestoreManager log--stop : $logData")
            return
        }

        db.collection(COLLECTION_LOGS)
            .document(COLLECTION_LOGS_DOCUMENT_DATE)
            .collection(getLogsDate())
            .add(
                getLogData(
                    context,
                    type,
                    message
                )
            )
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

    private fun getLogsDate(): String {
        val now = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return dateFormat.format(now)
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
            "appVersion" to getAppVersion(),
            "deviceID" to getDeviceId(context),
            "deviceModel" to getDeviceModel(),
            "deviceOS" to getDeviceOs(),
            "country" to getCountry(),
            "ip" to getIp()
        )
    }

    private fun getAppVersion(): String {
        return GlobalApplication.instance.appVersion
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

    private fun getIp(): String {
        return try {
            val networkInterfaces = NetworkInterface.getNetworkInterfaces()
            while (networkInterfaces.hasMoreElements()) {
                val networkInterface = networkInterfaces.nextElement()
                val inetAddresses = networkInterface.inetAddresses

                while (inetAddresses.hasMoreElements()) {
                    val inetAddress = inetAddresses.nextElement()
                    if (inetAddress.isLoopbackAddress) {
                        continue
                    }

                    if (inetAddress is Inet4Address) {
                        return inetAddress.hostAddress
                    }
                }
            }

            return "null"
        } catch (ex: Exception) {
            ex.toString()
        }
    }

}