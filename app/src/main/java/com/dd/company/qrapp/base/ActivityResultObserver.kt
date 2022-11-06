package com.dd.company.qrapp.base

import android.content.Intent

interface ActivityResultObserver {
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}