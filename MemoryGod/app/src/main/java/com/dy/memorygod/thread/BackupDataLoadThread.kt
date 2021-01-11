package com.dy.memorygod.thread

import android.content.Context
import com.dy.memorygod.db.MainDataDB
import com.dy.memorygod.entity.MainDataEntity

class BackupDataLoadThread(private val context: Context) : Thread() {

    var exception: String? = null
    var backupData: List<MainDataEntity>? = null

    override fun run() {
        super.run()

        try {
            val db = MainDataDB.getInstance(context)
            backupData = db?.mainDataDao()?.getAll() as List<MainDataEntity>
        } catch (ex: Exception) {
            exception = ex.toString()
        } finally {

        }
    }

}