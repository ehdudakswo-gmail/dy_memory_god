package com.dy.memorygod.thread

import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.dy.memorygod.data.MainData
import com.dy.memorygod.manager.ExcelManager
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import java.io.FileOutputStream

class ExcelFileSaveThread(
    private val context: Context,
    private val uri: Uri,
    private val dataList: List<MainData>
) : Thread() {

    var exception: String? = null
    private lateinit var workbook: SXSSFWorkbook
    private lateinit var parcelFileDescriptor: ParcelFileDescriptor
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

            parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "w")!!
            outputStream = FileOutputStream(parcelFileDescriptor.fileDescriptor)
            workbook.write(outputStream)
            outputStream.flush()
        } catch (ex: Exception) {
            exception = ex.toString()
        } finally {
            if (this::workbook.isInitialized) {
//                workbook.close()
            }

            if (this::parcelFileDescriptor.isInitialized) {
                parcelFileDescriptor.close()
            }

            if (this::outputStream.isInitialized) {
                outputStream.close()
            }
        }
    }

}