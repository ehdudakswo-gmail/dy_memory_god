package com.dy.memorygod.manager

import com.dy.memorygod.data.MainData

object MainDataManager {

    lateinit var dataList: ArrayList<MainData>
    lateinit var selectedData: MainData

    fun init() {
        this.dataList = ArrayList()
    }

    fun refreshBackup(backupDataList: ArrayList<MainData>) {
        dataList = backupDataList
    }

}