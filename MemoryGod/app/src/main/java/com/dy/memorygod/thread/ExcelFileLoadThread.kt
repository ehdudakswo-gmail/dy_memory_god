package com.dy.memorygod.thread

import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.DataType
import com.dy.memorygod.enums.TestCheck
import com.dy.memorygod.manager.ExcelManager
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.util.*

class ExcelFileLoadThread(private val file: File) : Thread() {

    var exception: String? = null
    lateinit var dataList: MutableList<MainData>

    private lateinit var workbook: HSSFWorkbook
    private lateinit var inputStream: FileInputStream

    override fun run() {
        super.run()

        try {
            dataList = mutableListOf()
            inputStream = FileInputStream(file)
            workbook = HSSFWorkbook(inputStream)

            val sheetIterator = workbook.sheetIterator()
            for (sheet in sheetIterator) {
                val title = sheet.sheetName.trim()
                val contentList = mutableListOf<MainDataContent>()
                val rowIterator = sheet.rowIterator()

                for (row in rowIterator) {
                    val problemColIdx = ExcelManager.problemColIdx
                    val answerColIdx = ExcelManager.answerColIdx
                    val problem = row.getCell(problemColIdx).toString().trim()
                    val answer = row.getCell(answerColIdx).toString().trim()

                    val content = MainDataContent(problem, answer, TestCheck.NONE)
                    contentList.add(content)
                }

                val mainData = MainData(title, contentList, Date(), DataType.NORMAL)
                dataList.add(mainData)
            }
        } catch (ex: Exception) {
            exception = ex.toString()
        } finally {
            if (this::workbook.isInitialized) {
                workbook.close()
            }

            if (this::inputStream.isInitialized) {
                inputStream.close()
            }
        }
    }

}