package com.dd.company.qrapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class History(
    var listData: MutableList<String> = mutableListOf()
) : Parcelable
