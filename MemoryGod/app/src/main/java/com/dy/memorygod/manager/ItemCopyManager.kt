package com.dy.memorygod.manager

import android.content.Context
import com.dy.memorygod.R
import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.DataType
import com.dy.memorygod.enums.DataTypePhone
import com.dy.memorygod.enums.TestCheck
import java.util.*

object ItemCopyManager {

    fun create(context: Context, originData: MainData): MainData {
        val titleFormat = context.getString(R.string.app_item_copy_title_format)
        val originTitle = originData.title

        val title = String.format(titleFormat, originTitle)
        val contentList = createContentList(originData.contentList)

        return MainData(title, contentList, Date(), DataType.NORMAL, DataTypePhone.NONE)
    }

    fun createContentList(originContentList: List<MainDataContent>): MutableList<MainDataContent> {
        val newContentList = mutableListOf<MainDataContent>()

        for (originContent in originContentList) {
            val originProblem = originContent.problem
            val originAnswer = originContent.answer
            val content = MainDataContent(originProblem, originAnswer, TestCheck.NONE)
            newContentList.add(content)
        }

        return newContentList
    }

}