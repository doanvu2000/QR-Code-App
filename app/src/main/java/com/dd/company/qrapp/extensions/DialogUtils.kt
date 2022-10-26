package com.dd.company.qrapp.extensions

import android.content.Context
import android.view.*
import com.dd.company.qrapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.showDialogConfirm(title: String?, message: String?, isShowCancel: Boolean = true, callbackPos: () -> Unit) {
    val builder = MaterialAlertDialogBuilder(this)
    builder.apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(getString(R.string.ok)) { _, _ ->
            callbackPos.invoke()
        }
        if (isShowCancel) {
            setNegativeButton(getString(R.string.text_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }
        show()
    }
}