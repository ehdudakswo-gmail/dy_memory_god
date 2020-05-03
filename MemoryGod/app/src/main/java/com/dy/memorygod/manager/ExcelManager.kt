package com.dy.memorygod.manager

import android.os.Environment
import java.io.File

object ExcelManager {

    val filePathType: String = Environment.DIRECTORY_DOWNLOADS
    val filePath: File =
        Environment.getExternalStoragePublicDirectory(filePathType)
    const val fileExtension = ".xlsx"
    const val fileMimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

    const val problemColIdx = 0
    const val answerColIdx = 1

    fun getFileList(): List<File> {
        val list = filePath.listFiles()
        val filteredList = list.filter { it.name.contains(fileExtension) }

        return filteredList.sortedBy { it.name }
    }

}