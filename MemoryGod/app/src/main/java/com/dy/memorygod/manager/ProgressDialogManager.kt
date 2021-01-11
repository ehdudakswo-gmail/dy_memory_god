package com.dy.memorygod.manager

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.dy.memorygod.R
import kotlinx.android.synthetic.main.dialog_main_progress_bar.view.*

object ProgressDialogManager {

    private lateinit var dialog: AlertDialog

    fun show(context: Context, message: String) {
        val activity = context as Activity
        if (activity.isFinishing) {
            return
        }

        val view = View.inflate(context, R.layout.dialog_main_progress_bar, null)
        val messageTextView = view.textView_main_progressBar_message
        messageTextView.text = message

        val builder = AlertDialog.Builder(context)
        dialog = builder
            .setView(view)
            .show()

        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
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