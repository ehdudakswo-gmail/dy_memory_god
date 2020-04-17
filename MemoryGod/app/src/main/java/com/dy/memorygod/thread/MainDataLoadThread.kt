package com.dy.memorygod.thread

import android.content.Context
import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.db.MainDataDB
import com.dy.memorygod.entity.MainDataEntity
import com.dy.memorygod.manager.MainDataManager
import java.util.*
import kotlin.collections.set

class MainDataLoadThread(val context: Context) : Thread() {

    var exception: String? = null

    override fun run() {
        super.run()

        try {
            val db = MainDataDB.getInstance(context)
            val entityList = db?.mainDataDao()?.getAll() as List<MainDataEntity>

            if (entityList.isEmpty()) {
                return
            }

            val hashMap = HashMap<Int, MainData>()
            for (entity in entityList) {
                val idx = entity.idx
                var contentList: MutableList<MainDataContent>

                when (hashMap.containsKey(idx)) {
                    true -> {
                        val existingData = hashMap[idx]
                        contentList = existingData?.contentList as MutableList<MainDataContent>
                    }
                    false -> {
                        val newData = MainData(
                            entity.title,
                            mutableListOf(),
                            entity.updatedDate,
                            entity.dataType,
                            entity.dataTypePhone,
                            idx
                        )

                        hashMap[idx] = newData
                        contentList = newData.contentList
                    }
                }

                if (entity.hasContent) {
                    val content = MainDataContent(entity.problem, entity.answer, entity.testCheck)
                    contentList.add(content)
                }
            }

            val dataList = hashMap.values.sortedBy { it.idx }.toMutableList()
            MainDataManager.refresh(dataList)
        } catch (ex: Exception) {
            exception = ex.toString()
        } finally {

        }
    }

}