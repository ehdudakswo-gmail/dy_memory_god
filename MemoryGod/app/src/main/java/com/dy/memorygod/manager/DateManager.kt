package com.dy.memorygod.manager

import java.text.SimpleDateFormat
import java.util.*

object DateManager {

    fun getExcelFileName(): String {
        val pattern = "yyyy-MM-dd HH.mm.ss"
        val locale = Locale.getDefault()

        return SimpleDateFormat(pattern, locale).format(Date())
    }

    fun getPhoneNumberActive(): Any {
        return getExcelFileName()
    }

}