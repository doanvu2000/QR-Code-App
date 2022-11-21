package com.dd.company.qrapp.views

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.dd.company.qrapp.base.BaseActivity
import com.dd.company.qrapp.base.RESULT
import com.dd.company.qrapp.databinding.ActivityResultBinding
import com.dd.company.qrapp.extensions.getAdSizeFollowScreen
import com.dd.company.qrapp.extensions.setOnSafeClick
import com.dd.company.qrapp.utils.openActivity
import com.dd.company.qrapp.views.main.HistoryActivity
import com.google.android.gms.ads.*


class ResultActivity : BaseActivity<ActivityResultBinding>() {

    private var result = ""
    override fun initView() {
        initData()
        binding.tvResult.text = result
        supportActionBar?.hide()
        showAds()
    }

    private fun showAds() {
        val adRequest = AdRequest.Builder()
            .build()
        if (binding.adView.childCount > 0) return
        val adView = AdView(this)
        adView.apply {
            adUnitId = "ca-app-pub-7304974533758848/5274950434"
            setAdSize(getAdSizeFollowScreen())
            loadAd(adRequest)
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                }

                override fun onAdClosed() {
                }

                fun onAdFailedToLoad(errorCode: Int) {
                }

                fun onAdLeftApplication() {
                }

                override fun onAdOpened() {
                }
            }
        }
        binding.adView.addView(adView)
    }

    override fun initListener() {
        binding.apply {
            btnCopy.setOnSafeClick {
                val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(RESULT, result)
                clipboard.setPrimaryClip(clip)
            }
            tvOpenBrowser.setOnSafeClick { openBrowser() }
            btnOpenBrowser.setOnSafeClick { openBrowser() }
            btnShare.setOnSafeClick { shareData() }
            tvShare.setOnSafeClick { shareData() }
            btnBack.setOnSafeClick { onBackPressed() }
            btnHistory.setOnSafeClick { openActivity(HistoryActivity::class.java) }
        }
    }

    private fun shareData() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Result QR")
        intent.putExtra(Intent.EXTRA_TEXT, result)
        startActivity(Intent.createChooser(intent, "Share result QR"))
    }

    private fun openBrowser() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(result)))
        } catch (e: Exception) {
            Toast.makeText(this@ResultActivity, "Cannot open browser with $result", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    override fun initData() {
        result = intent.getStringExtra(RESULT) ?: ""
    }
}