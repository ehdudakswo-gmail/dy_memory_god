package com.dy.memorygod.manager

import com.dy.memorygod.data.MainData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonManager {

    fun toJson(src: Any): String {
        return Gson().toJson(src)
    }

    fun toMainBackupDataList(json: String): ArrayList<MainData> {
        val listType: TypeToken<ArrayList<MainData>> =
            object : TypeToken<ArrayList<MainData>>() {}

        return Gson().fromJson(json, listType.type)
    }

    fun toMainData(json: String): MainData {
        return Gson().fromJson(json, MainData::class.java)
    }

}