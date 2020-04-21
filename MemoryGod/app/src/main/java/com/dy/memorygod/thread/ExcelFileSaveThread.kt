package com.dy.memorygod.thread

import android.content.Context
import com.dy.memorygod.data.MainData
import com.dy.memorygod.manager.ExcelManager
import java.io.File

class ExcelFileSaveThread(
    private val context: Context,
    private val dataList: List<MainData>,
    private val fileName: String
) : Thread() {

    var exception: String? = null
    lateinit var file: File

    override fun run() {
        super.run()

        try {
            file = ExcelManager.save(context, dataList, fileName)
        } catch (ex: Exception) {
            exception = ex.toString()
        } finally {
            ExcelManager.close()
        }
    }

}