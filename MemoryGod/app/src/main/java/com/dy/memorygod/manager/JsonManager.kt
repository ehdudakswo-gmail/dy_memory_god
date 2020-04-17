package com.dy.memorygod.manager

import com.dy.memorygod.enums.DataType
import com.dy.memorygod.enums.DataTypePhone
import com.dy.memorygod.enums.TestCheck
import com.google.gson.Gson

object JsonManager {

    fun toJson(src: Any): String {
        return Gson().toJson(src)
    }

    fun toDataType(json: String): DataType {
        return Gson().fromJson(json, DataType::class.java)
    }

    fun toDataTypePhone(json: String): DataTypePhone {
        return Gson().fromJson(json, DataTypePhone::class.java)
    }

    fun toTestCheck(json: String): TestCheck {
        return Gson().fromJson(json, TestCheck::class.java)
    }

}