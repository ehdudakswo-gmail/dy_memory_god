package com.dy.memorygod.thread

import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.DataType
import com.dy.memorygod.enums.DataTypePhone
import com.dy.memorygod.enums.TestCheck
import com.dy.memorygod.manager.ExcelManager
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.util.*

class ExcelFileLoadThread(private val file: File) : Thread() {

    var exception: String? = null
    lateinit var dataList: MutableList<MainData>

    private lateinit var workbook: XSSFWorkbook
    private lateinit var inputStream: FileInputStream

    override fun run() {
        super.run()

        try {
            dataList = mutableListOf()
            inputStream = FileInputStream(file)
            workbook = XSSFWorkbook(inputStream)

            val sheetIterator = workbook.iterator()
            for (sheet in sheetIterator) {
                val title = sheet.sheetName.trim()
                val contentList = mutableListOf<MainDataContent>()
                val rowIterator = sheet.rowIterator()

                for (row in rowIterator) {
                    val problemColIdx = ExcelManager.problemColIdx
                    val answerColIdx = ExcelManager.answerColIdx

                    val problemCell = row.getCell(problemColIdx)
                    val answerCell = row.getCell(answerColIdx)

                    if (problemCell == null || answerCell == null) {
                        continue
                    }

                    var problem = problemCell.toString().trim()
                    var answer = answerCell.toString().trim()

                    problem.toDoubleOrNull()?.let {
                        if (it == kotlin.math.floor(it)) {
                            problem = it.toLong().toString()
                        }
                    }

                    answer.toDoubleOrNull()?.let {
                        if (it == kotlin.math.floor(it)) {
                            answer = it.toLong().toString()
                        }
                    }

                    val content = MainDataContent(problem, answer, TestCheck.NONE)
                    contentList.add(content)
                }

                val mainData =
                    MainData(title, contentList, Date(), DataType.NORMAL, DataTypePhone.NONE)
                dataList.add(mainData)
            }
        } catch (ex: Exception) {
            exception = ex.toString()
        } finally {
            if (this::workbook.isInitialized) {
//                workbook.close()
            }

            if (this::inputStream.isInitialized) {
                inputStream.close()
            }
        }
    }

}