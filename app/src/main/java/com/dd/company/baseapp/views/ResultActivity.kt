package com.dd.company.baseapp.views

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.dd.company.baseapp.base.BaseActivity
import com.dd.company.baseapp.base.RESULT
import com.dd.company.baseapp.databinding.ActivityResultBinding


class ResultActivity : BaseActivity<ActivityResultBinding>() {

    private var result = ""
    override fun initView() {
        initData()
        binding.tvResult.text = result
    }

    override fun initListener() {
        binding.apply {
            btnCopy.setOnClickListener {
                val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(RESULT, result)
                clipboard.setPrimaryClip(clip)
            }
            btnOpenBrowser.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(result)))
            }
            btnShare.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Result QR")
                intent.putExtra(Intent.EXTRA_TEXT, result)
                startActivity(Intent.createChooser(intent, "Share result QR"))
            }
        }
    }

    override fun initData() {
        result = intent.getStringExtra(RESULT) ?: ""
    }
}