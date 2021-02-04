package com.dy.memorygod.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dy.memorygod.R
import com.dy.memorygod.enums.LogType
import com.dy.memorygod.enums.VersionCheckState
import com.dy.memorygod.manager.FirebaseLogManager
import com.dy.memorygod.manager.NetworkCheckManager
import com.dy.memorygod.thread.MarketVersionThread

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        checkNetwork()
    }

    private fun checkNetwork() {
        if (NetworkCheckManager.isConnected(this)) {
            checkVersion()
        } else {
            Toast.makeText(this, R.string.home_network_not_connected, Toast.LENGTH_SHORT).show()
            startMainActivity()
        }
    }

    private fun checkVersion() {
        val appVersion = getAppVersion()
        val marketVersion = getMarketVersion()

        if (appVersion == null) {
            Toast.makeText(this, R.string.home_app_version_null, Toast.LENGTH_SHORT).show()
            startMainActivity()
            return
        }
        if (marketVersion == null) {
            Toast.makeText(this, R.string.home_market_version_null, Toast.LENGTH_SHORT).show()
            startMainActivity()
            return
        }

        val state = getVersionCheckState(appVersion, marketVersion)
        when (state) {
            VersionCheckState.NORMAL -> {
                startMainActivity()
            }
            VersionCheckState.CHECK_ERROR -> {
                Toast.makeText(this, R.string.home_version_check_error, Toast.LENGTH_SHORT).show()
                startMainActivity()
            }
            VersionCheckState.UPDATE_REQUEST -> {
                showUpdateAlertDialog(appVersion, marketVersion)
            }
        }
    }

    private fun getAppVersion(): String? {
        return try {
            val packageInfo =
                packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
            packageInfo.versionName
        } catch (ex: Exception) {
            null
        }
    }

    private fun getMarketVersion(): String? {
        return try {
            val thread = MarketVersionThread(packageName)

            thread.start()
            thread.join()
            thread.getVersion()
        } catch (ex: java.lang.Exception) {
            null
        }
    }

    private fun getVersionCheckState(appVersion: String, marketVersion: String): VersionCheckState {
        try {
            val checkLen = 3
            val delimiters = "."
            val appVersionArr = appVersion.trim().split(delimiters)
            val marketVersionArr = marketVersion.trim().split(delimiters)

            if (appVersionArr.size != checkLen) {
                return VersionCheckState.CHECK_ERROR
            }
            if (marketVersionArr.size != checkLen) {
                return VersionCheckState.CHECK_ERROR
            }

            for (i in 0 until checkLen) {
                val app = appVersionArr[i].toInt()
                val market = marketVersionArr[i].toInt()

                if (app < market) {
                    return VersionCheckState.UPDATE_REQUEST
                }
                if (app > market) {
                    return VersionCheckState.NORMAL
                }
            }
        } catch (ex: Exception) {
            return VersionCheckState.CHECK_ERROR
        }

        return VersionCheckState.NORMAL
    }

    private fun showUpdateAlertDialog(appVersion: String, marketVersion: String) {
        val builder = AlertDialog.Builder(this)
        val dialog = builder
            .setTitle(R.string.home_version_update_alert_dialog_title)
            .setMessage(getUpdateMessage(appVersion, marketVersion))
            .setPositiveButton(R.string.home_version_update_alert_dialog_button_ok) { _, _ ->
                setUpdateLog(LogType.HOME_UPDATE_OK_CLICK, appVersion, marketVersion)
                startMarket()
                finish()
            }
            .setNegativeButton(R.string.app_dialog_cancel) { _, _ ->
                setUpdateLog(
                    LogType.HOME_UPDATE_CANCEL_CLICK,
                    appVersion,
                    marketVersion
                )
                startMainActivity()
            }
            .show()

        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
    }

    private fun getUpdateMessage(appVersion: String, marketVersion: String): String {
        val format = getString(R.string.home_version_update_alert_dialog_message_format)
        return String.format(format, appVersion, marketVersion)
    }

    private fun setUpdateLog(
        logType: LogType,
        appVersion: String,
        marketVersion: String
    ) {
        // Log Data
        val logMessageArr = arrayOf("appVersion : $appVersion", "marketVersion : $marketVersion")
        val logMessage = FirebaseLogManager.getJoinData(logMessageArr)

        // Firebase Log
        FirebaseLogManager.log(this, logType, logMessage)
    }

    private fun startMarket() {
        try {
            val action = Intent.ACTION_VIEW
            val uri = Uri.parse("market://details?id=$packageName")

            val intent = Intent(action, uri)
            startActivity(intent)
        } catch (ex: Exception) {
            val action = Intent.ACTION_VIEW
            val uri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")

            val intent = Intent(action, uri)
            startActivity(intent)
        }
    }

    private fun startMainActivity() {
        Handler().postDelayed({
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 500)
    }

}