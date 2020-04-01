package com.dy.memorygod.data

import com.dy.memorygod.enums.DataType
import com.dy.memorygod.enums.DataTypePhone
import java.util.*

data class MainData(
    var title: String,
    val contentList: ArrayList<MainDataContent>,
    var updatedDate: Date?,
    val dataType: DataType,
    val dataTypePhone: DataTypePhone = DataTypePhone.NONE
)