package com.dy.memorygod.manager

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.dy.memorygod.R

object AlertDialogManager {

    private lateinit var dialog: AlertDialog

    fun show(context: Context, message: String) {
        val activity = context as Activity
        if (activity.isFinishing) {
            return
        }

        dialog = AlertDialog.Builder(context)
            .setMessage(message)
            .setPositiveButton(R.string.app_dialog_ok, null)
            .show()

        dialog.setCanceledOnTouchOutside(false)
    }

    fun hide(context: Context) {
        val activity = context as Activity
        if (activity.isFinishing) {
            return
        }

        if (!this::dialog.isInitialized) {
            return
        }

        if (!dialog.isShowing) {
            return
        }

        dialog.dismiss()
        dialog.cancel()
    }

}