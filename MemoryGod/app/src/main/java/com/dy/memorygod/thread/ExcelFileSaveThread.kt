package com.dy.memorygod.thread

import com.dy.memorygod.data.MainData
import com.dy.memorygod.manager.ExcelManager
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class ExcelFileSaveThread(
    private val dataList: List<MainData>,
    private val filePath: File,
    private val fileName: String
) : Thread() {

    var exception: String? = null
    lateinit var file: File

    private lateinit var workbook: SXSSFWorkbook
    private lateinit var outputStream: FileOutputStream

    override fun run() {
        super.run()

        try {
            workbook = SXSSFWorkbook()

            for (data in dataList) {
                val title = data.title
                val contentList = data.contentList
                val sheet = workbook.createSheet(title)

                for (i in 0 until contentList.size) {
                    val content = contentList[i]
                    val problem = content.problem
                    val answer = content.answer

                    val row = sheet.createRow(i)
                    val problemColIdx = ExcelManager.problemColIdx
                    val answerColIdx = ExcelManager.answerColIdx
                    val problemCell = row.createCell(problemColIdx)
                    val answerCell = row.createCell(answerColIdx)

                    problemCell.setCellValue(problem)
                    answerCell.setCellValue(answer)
                }
            }

            file = File(filePath, fileName)
            outputStream = FileOutputStream(file)
            workbook.write(outputStream)
            outputStream.flush()
        } catch (ex: Exception) {
            exception = ex.toString()
        } finally {
            if (this::workbook.isInitialized) {
//                workbook.close()
            }

            if (this::outputStream.isInitialized) {
                outputStream.close()
            }
        }
    }

}