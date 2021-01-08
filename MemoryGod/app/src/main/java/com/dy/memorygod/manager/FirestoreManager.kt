package com.dy.memorygod.manager

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.dy.memorygod.GlobalApplication
import com.dy.memorygod.enums.FirestoreLogType
import java.net.Inet4Address
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*


object FirestoreManager {

    const val COLLECTION_CONFIG = "config"
    const val COLLECTION_CONFIG_DOCUMENT_FIRESTORE = "firestore"
    const val COLLECTION_CONFIG_DOCUMENT_FIRESTORE_FIELD_isAllEnable = "isAllEnable"
    const val COLLECTION_CONFIG_DOCUMENT_FIRESTORE_FIELD_isLogEnable = "isLogEnable"

    const val COLLECTION_LOGS = "logs"
    const val COLLECTION_LOGS_DOCUMENT_DATE = "date"

    fun isLogEnable(): Boolean {
        val appConfig = GlobalApplication.instance.firestoreConfig
        if (!appConfig.isAllEnable) {
            return false
        }

        if (!appConfig.isLogEnable) {
            return false
        }

        return true
    }

    fun getLogsDate(): String {
        val now = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        return dateFormat.format(now)
    }

    fun getLogData(
        context: Context,
        type: FirestoreLogType,
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