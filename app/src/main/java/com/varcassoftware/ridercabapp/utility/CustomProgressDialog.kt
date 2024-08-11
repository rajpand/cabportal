package com.varcassoftware.ridercabapp.util

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.varcassoftware.ridercabapp.R

class CustomProgressDialog(private val context: Context) {
    private var dialog: Dialog? = null

    fun show() {
        if (dialog == null) {
            dialog = Dialog(context)
            val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null)
            dialog?.setContentView(view)
            dialog?.setCancelable(false)
            dialog?.setCanceledOnTouchOutside(false)
        }
        dialog?.show()
    }

    fun dismiss() {
        dialog?.dismiss()
    }
}
