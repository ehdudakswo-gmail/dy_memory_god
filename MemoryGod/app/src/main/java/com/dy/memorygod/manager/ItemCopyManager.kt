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
        val contentList = mutableListOf<MainDataContent>()

        for (originContentList in originData.contentList) {
            val originProblem = originContentList.problem
            val originAnswer = originContentList.answer
            val content = MainDataContent(originProblem, originAnswer, TestCheck.NONE)
            contentList.add(content)
        }

        return MainData(title, contentList, Date(), DataType.NORMAL, DataTypePhone.NONE)
    }

}