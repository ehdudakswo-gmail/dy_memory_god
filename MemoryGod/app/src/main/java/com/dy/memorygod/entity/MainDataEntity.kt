package com.dy.memorygod.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dy.memorygod.enums.DataType
import com.dy.memorygod.enums.DataTypePhone
import com.dy.memorygod.enums.TestCheck
import java.util.*

@Entity(tableName = "main_data_table")
class MainDataEntity(
    @PrimaryKey var id: Long,
    @ColumnInfo(name = "idx") var idx: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "updatedDate") var updatedDate: Date?,
    @ColumnInfo(name = "dataType") var dataType: DataType,
    @ColumnInfo(name = "dataTypePhone") var dataTypePhone: DataTypePhone,
    @ColumnInfo(name = "hasContent") var hasContent: Boolean = false,
    @ColumnInfo(name = "problem") var problem: String = "",
    @ColumnInfo(name = "answer") var answer: String = "",
    @ColumnInfo(name = "testCheck") var testCheck: TestCheck = TestCheck.NONE
)