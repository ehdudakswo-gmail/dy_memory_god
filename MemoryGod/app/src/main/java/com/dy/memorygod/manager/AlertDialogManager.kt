package com.dy.memorygod.manager

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.dy.memorygod.R

object AlertDialogManager {

    fun show(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        val dialog = builder
            .setMessage(message)
            .setPositiveButton(R.string.app_dialog_ok, null)
            .show()

        dialog.setCanceledOnTouchOutside(false)
    }

}