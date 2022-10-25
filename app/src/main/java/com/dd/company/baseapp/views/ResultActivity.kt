package com.dd.company.baseapp.views

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.dd.company.baseapp.base.BaseActivity
import com.dd.company.baseapp.base.RESULT
import com.dd.company.baseapp.databinding.ActivityResultBinding
import com.dd.company.baseapp.extensions.setOnSafeClick


class ResultActivity : BaseActivity<ActivityResultBinding>() {

    private var result = ""
    override fun initView() {
        initData()
        binding.tvResult.text = result
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