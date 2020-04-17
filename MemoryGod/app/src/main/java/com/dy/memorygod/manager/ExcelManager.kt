package com.dy.memorygod.manager

import android.content.Context
import android.os.Environment
import com.dy.memorygod.R
import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.DataType
import com.dy.memorygod.enums.TestCheck
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList

object ExcelManager {

    val filePathType: String = Environment.DIRECTORY_DOWNLOADS
    const val fileExtension = ".xls"

    private val filePath =
        Environment.getExternalStoragePublicDirectory(filePathType)
    private const val problemColIdx = 0
    private const val answerColIdx = 1

    private lateinit var workbook: HSSFWorkbook
    private lateinit var outputStream: FileOutputStream
    private lateinit var inputStream: FileInputStream

    fun save(context: Context, fileName: String, dataList: List<MainData>): File {
//        val workbook = SXSSFWorkbook()
        workbook = HSSFWorkbook()

        for (data in dataList) {
            val title = getTitle(context, data)
            val contentList = data.contentList
            val sheet = workbook.createSheet(title)

            for (i in 0 until contentList.size) {
                val content = contentList[i]
                val problem = content.problem
                val answer = content.answer

                val row = sheet.createRow(i)
                val problemCell = row.createCell(problemColIdx)
                val answerCell = row.createCell(answerColIdx)

                problemCell.setCellValue(problem)
                answerCell.setCellValue(answer)
            }
        }

        val file = File(filePath, fileName)
        outputStream = FileOutputStream(file)
        workbook.write(outputStream)
        outputStream.flush()

        return file
    }

    private fun getTitle(context: Context, data: MainData): String {
        return when (data.dataType) {
            DataType.NORMAL -> {
                data.title
            }
            DataType.PHONE -> {
                val format = context.getString(R.string.app_file_copy_title_format)
                String.format(format, data.title)
            }
        }
    }

    fun load(file: File): List<MainData> {
        inputStream = FileInputStream(file)
        workbook = HSSFWorkbook(inputStream)

        val dataList = ArrayList<MainData>()
        val sheetIterator = workbook.sheetIterator()

        for (sheet in sheetIterator) {
            val title = sheet.sheetName.trim()
            val contentList = ArrayList<MainDataContent>()
            val rowIterator = sheet.rowIterator()

            for (row in rowIterator) {
                val problem = row.getCell(problemColIdx).toString().trim()
                val answer = row.getCell(answerColIdx).toString().trim()

                val content = MainDataContent(problem, answer, TestCheck.NONE)
                contentList.add(content)
            }

            val mainData = MainData(title, contentList, Date(), DataType.NORMAL)
            dataList.add(mainData)
        }

        return dataList
    }

    fun close() {
        if (this::workbook.isInitialized) {
            workbook.close()
        }

        if (this::outputStream.isInitialized) {
            outputStream.close()
        }

        if (this::inputStream.isInitialized) {
            inputStream.close()
        }
    }

    fun getFileList(): List<File> {
        val list = filePath.listFiles()
        val filteredList = list.filter { it.name.contains(fileExtension) }

        return filteredList.sortedBy { it.name }
    }

}