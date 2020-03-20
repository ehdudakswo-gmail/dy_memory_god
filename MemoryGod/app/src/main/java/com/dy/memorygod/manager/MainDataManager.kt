package com.dy.memorygod.manager

import com.dy.memorygod.data.MainData

object MainDataManager {

    lateinit var dataList: ArrayList<MainData>
    lateinit var selectedData: MainData

    fun init() {
        this.dataList = ArrayList()
    }

    fun getBackupDataList(): List<MainData> {
        return dataList.filterNot { it.isPhoneData }
    }

    fun refreshBackup(backupDataList: List<MainData>) {
        val newDataList = ArrayList<MainData>()
        val phoneDataList = dataList.filter { it.isPhoneData }

        newDataList.addAll(phoneDataList)
        newDataList.addAll(backupDataList)

        dataList = newDataList
    }

}