package com.dy.memorygod.manager

import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent

object MainDataManager {

    lateinit var dataList: ArrayList<MainData>
    lateinit var selectedData: MainData
    var searchData: MainData? = null
    var searchContentData: MainDataContent? = null

    fun init() {
        this.dataList = ArrayList()
    }

    fun refreshBackup(backupDataList: ArrayList<MainData>) {
        dataList = backupDataList
    }

}