package com.dy.memorygod.manager

import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent

object MainDataManager {

    var isLoadingComplete = false
    lateinit var dataList: MutableList<MainData>
    lateinit var selectedData: MainData
    var searchData: MainData? = null
    var searchContentData: MainDataContent? = null

    fun init() {
        this.isLoadingComplete = false
        this.dataList = mutableListOf()
    }

    fun refresh(dataList: MutableList<MainData>) {
        this.dataList = dataList
    }

    fun setLoadingComplete() {
        isLoadingComplete = true
    }

}