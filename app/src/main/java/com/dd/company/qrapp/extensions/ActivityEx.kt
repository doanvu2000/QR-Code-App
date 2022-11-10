package com.dd.company.qrapp.extensions

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdSize
import kotlinx.coroutines.flow.Flow

fun <T> AppCompatActivity.collectFlow(targetFlow: Flow<T>, collectBlock: ((T) -> Unit)) {
    lifecycleScope.launchWhenStarted {
        targetFlow.collect {
            collectBlock(it)
        }
    }
}

fun Activity.getAdSizeFollowScreen(): AdSize {
    val display = this.windowManager.defaultDisplay
    val outMetrics = DisplayMetrics()
    display.getMetrics(outMetrics)
    val density = outMetrics.density
    var adWidthPixels = resources.displayMetrics.widthPixels.toFloat()
    if (adWidthPixels == 0f) {
        adWidthPixels = outMetrics.widthPixels.toFloat()
    }
    val adWidth = (adWidthPixels / density).toInt()
    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
}