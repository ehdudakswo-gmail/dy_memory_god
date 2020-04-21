package com.dy.memorygod.thread

import com.dy.memorygod.data.MainData
import com.dy.memorygod.manager.ExcelManager
import java.io.File

class ExcelFileLoadThread(private val file: File) : Thread() {

    var exception: String? = null
    lateinit var dataList: List<MainData>

    override fun run() {
        super.run()

        try {
            dataList = ExcelManager.load(file)
        } catch (ex: Exception) {
            exception = ex.toString()
        } finally {
            ExcelManager.close()
        }
    }

}