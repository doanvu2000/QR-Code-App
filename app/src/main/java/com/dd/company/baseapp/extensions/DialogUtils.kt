package com.dd.company.baseapp.extensions

import android.content.Context
import android.view.*
import com.dd.company.baseapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.showAlertDialogConfirm(title: String?, message: String?, callback:()->Unit) {
    val builder = MaterialAlertDialogBuilder(this)
    builder.apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(getString(R.string.ok)) { _, _ ->
            callback.invoke()
        }
        show()
    }
}