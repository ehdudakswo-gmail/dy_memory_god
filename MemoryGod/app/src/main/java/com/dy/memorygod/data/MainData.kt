package com.dy.memorygod.data

import com.dy.memorygod.enums.DataType
import java.util.*

data class MainData(
    val dataType: DataType,
    var title: String,
    val contentList: ArrayList<MainDataContent>,
    var updatedDate: Date?
)