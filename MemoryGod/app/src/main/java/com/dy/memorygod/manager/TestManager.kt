package com.dy.memorygod.manager

import android.content.Context
import com.dy.memorygod.R
import java.util.*

object TestManager {

    fun getWrongResult(context: Context, user: String, data: String): String {
        val userRaw = user.replace(" ", "")
        val dataRaw = data.replace(" ", "")
        if (userRaw == dataRaw) {
            return context.getString(R.string.test_item_test_dialog_wrong_space)
        }

        val userLower = user.toLowerCase(Locale.ROOT)
        val dataLower = data.toLowerCase(Locale.ROOT)
        if (userLower == dataLower) {
            return context.getString(R.string.test_item_test_dialog_wrong_case_sensitive)
        }

        return context.getString(R.string.test_item_test_dialog_wrong)
    }

}