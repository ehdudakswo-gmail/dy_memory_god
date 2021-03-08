package com.dy.memorygod.thread

import android.content.Context
import com.dy.memorygod.data.MainData
import com.dy.memorygod.data.MainDataContent
import com.dy.memorygod.enums.DataType
import com.dy.memorygod.enums.DataTypePhone
import com.dy.memorygod.enums.TestCheck
import com.dy.memorygod.manager.AndroidLogManager
import com.dy.memorygod.manager.MainDataManager
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*

class SampleDataLoadThread(
    private val context: Context
) : Thread() {

    var exception: String? = null
    private val assetsPath = "sample"
    private val fileSplitStart = "__"
    private val fileSplitEnd = ".csv"

    private lateinit var inputStream: InputStream
    private lateinit var inputStreamReader: InputStreamReader
    private lateinit var bufferedReader: BufferedReader

    override fun run() {
        super.run()

        try {
            val list = context.assets.list(assetsPath) ?: return
            val sortedList = list.sortedBy { it }

            for (file in sortedList) {
                setSampleData(file)
                AndroidLogManager.d("SampleDataLoadThread file : $file")
            }
        } catch (ex: Exception) {
            exception = ex.toString()
            AndroidLogManager.d("SampleDataLoadThread ex : $ex")
        }
    }

    private fun setSampleData(file: String) {
        try {
            val fileName = "${assetsPath}/${file}"
            inputStream = context.assets.open(fileName)
            inputStreamReader = InputStreamReader(inputStream)
            bufferedReader = BufferedReader(inputStreamReader)

            val contentList = mutableListOf<MainDataContent>()
            while (true) {
                val line = bufferedReader.readLine() ?: break
                val idx = line.indexOf(",")
                if (idx == -1) {
                    continue
                }

                val rawElements = arrayOf(line.substring(0, idx), line.substring(idx + 1))
                val validElements = rawElements.map { it.replace("\"", "").trim() }

                val problem = validElements[0]
                val answer = validElements[1]

                contentList.add(
                    MainDataContent(
                        problem,
                        answer,
                        TestCheck.NONE
                    )
                )
            }

            if (contentList.isEmpty()) {
                return
            }

            val startIdx = file.indexOf(fileSplitStart) + fileSplitStart.length
            val endIdx = file.indexOf(fileSplitEnd)
            val title = file.substring(startIdx, endIdx)

            val data = MainData(title, contentList, Date(), DataType.NORMAL, DataTypePhone.NONE)
            MainDataManager.dataList.add(data)

//            LogsManager.d("SampleDataLoadThread data : $data")
        } catch (ex: Exception) {
            throw ex
        } finally {
            if (this::inputStream.isInitialized) {
                inputStream.close()
            }
            if (this::inputStreamReader.isInitialized) {
                inputStreamReader.close()
            }
            if (this::bufferedReader.isInitialized) {
                bufferedReader.close()
            }
        }
    }

}