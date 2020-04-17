package com.dy.memorygod.thread

import android.content.Context
import com.dy.memorygod.db.MainDataDB
import com.dy.memorygod.entity.MainDataEntity
import com.dy.memorygod.manager.MainDataManager

class MainDataSaveThread(val context: Context) : Thread() {

    var exception: String? = null

    override fun run() {
        super.run()

        try {
            val dataList = MainDataManager.dataList
            val db = MainDataDB.getInstance(context)
            db?.mainDataDao()?.deleteAll()

            var id = 0L
            for (idx in 0 until dataList.size) {
                val data = dataList[idx]
                val title = data.title
                val contentList = data.contentList
                val updatedDate = data.updatedDate
                val dataType = data.dataType
                val dataTypePhone = data.dataTypePhone

                if (contentList.isEmpty()) {
                    val entity = MainDataEntity(
                        id,
                        idx,
                        title,
                        updatedDate,
                        dataType,
                        dataTypePhone
                    )

                    db?.mainDataDao()?.insert(entity)
                    id++
                } else {
                    for (content in contentList) {
                        val entity = MainDataEntity(
                            id,
                            idx,
                            title,
                            updatedDate,
                            dataType,
                            dataTypePhone,
                            true,
                            content.problem,
                            content.answer,
                            content.testCheck
                        )

                        db?.mainDataDao()?.insert(entity)
                        id++
                    }
                }
            }
        } catch (ex: Exception) {
            exception = ex.toString()
        } finally {

        }
    }

}