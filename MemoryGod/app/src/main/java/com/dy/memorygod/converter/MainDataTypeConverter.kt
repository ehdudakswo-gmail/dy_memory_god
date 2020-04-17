package com.dy.memorygod.converter

import androidx.room.TypeConverter
import com.dy.memorygod.enums.DataType
import com.dy.memorygod.enums.DataTypePhone
import com.dy.memorygod.enums.TestCheck
import com.dy.memorygod.manager.JsonManager
import java.util.*

class MainDataTypeConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromDataType(dataType: DataType): String {
        return JsonManager.toJson(dataType)
    }

    @TypeConverter
    fun toDataType(json: String): DataType {
        return JsonManager.toDataType(json)
    }

    @TypeConverter
    fun fromDataTypePhone(dataTypePhone: DataTypePhone): String {
        return JsonManager.toJson(dataTypePhone)
    }

    @TypeConverter
    fun toDataTypePhone(json: String): DataTypePhone {
        return JsonManager.toDataTypePhone(json)
    }

    @TypeConverter
    fun fromTestCheck(testCheck: TestCheck): String {
        return JsonManager.toJson(testCheck)
    }

    @TypeConverter
    fun toTestCheck(json: String): TestCheck {
        return JsonManager.toTestCheck(json)
    }

}