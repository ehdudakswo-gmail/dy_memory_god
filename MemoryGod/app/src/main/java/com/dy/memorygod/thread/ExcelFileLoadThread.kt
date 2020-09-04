package com.dy.memorygod.thread

import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.DataType
import com.dy.memorygod.enums.DataTypePhone
import com.dy.memorygod.enums.TestCheck
import com.dy.memorygod.manager.ExcelManager
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.util.*

class ExcelFileLoadThread(private val context: Context, private val uri: Uri) : Thread() {

    var exception: String? = null
    lateinit var dataList: MutableList<MainData>

    private lateinit var parcelFileDescriptor: ParcelFileDescriptor
    private lateinit var inputStream: FileInputStream
    private lateinit var workbook: XSSFWorkbook

    override fun run() {
        super.run()

        try {
            dataList = mutableListOf()
            parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r")!!
            inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
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

                    if (isIntValue(problem)) {
                        problem = toIntValue(problem)
                    }

                    if (isIntValue(answer)) {
                        answer = toIntValue(answer)
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

            if (this::parcelFileDescriptor.isInitialized) {
                parcelFileDescriptor.close()
            }

            if (this::inputStream.isInitialized) {
                inputStream.close()
            }
        }
    }

    private fun isIntValue(str: String): Boolean {
        str.toDoubleOrNull()?.let { doubleValue ->
            return doubleValue == kotlin.math.floor(doubleValue)
        }

        return false
    }

    private fun toIntValue(str: String): String {
        val pointIdx = str.indexOf(".")
        if (pointIdx != -1) {
            return str.substring(0, pointIdx)
        }

        return str
    }

}