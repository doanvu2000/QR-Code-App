package com.dd.company.qrapp.views.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import com.dd.company.qrapp.R
import com.dd.company.qrapp.base.BaseActivity
import com.dd.company.qrapp.base.HISTORY
import com.dd.company.qrapp.base.RESULT
import com.dd.company.qrapp.databinding.ActivityHistoryBinding
import com.dd.company.qrapp.extensions.getAdSizeFollowScreen
import com.dd.company.qrapp.model.History
import com.dd.company.qrapp.pref.LocalCache
import com.dd.company.qrapp.views.adapter.HistoryAdapter
import com.dd.company.qrapp.widget.dialog.AlertMessageDialog
import com.dd.company.qrapp.widget.recyclerview.RecyclerUtils
import com.google.android.gms.ads.*

class HistoryActivity : BaseActivity<ActivityHistoryBinding>() {

    private val adapter by lazy {
        HistoryAdapter()
    }

    override fun initView() {
        supportActionBar?.hide()
        initRecyclerView()
        binding.toolbar.setTitleColor(R.color.white)
        showAds()
    }

    private fun showAds() {
        val adRequest = AdRequest.Builder()
            .build()
        Log.d("dddd", "is test device: ${adRequest.isTestDevice(this)}")
        if (binding.adView.childCount > 0) return
        val adView = AdView(this)
        adView.apply {
            adUnitId = "ca-app-pub-7304974533758848/5274950434"
            setAdSize(getAdSizeFollowScreen())
            loadAd(adRequest)
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.d("dddd", "Ad is loaded!")
                }

                override fun onAdClosed() {
                    Log.d("dddd", "Ad is closed!")
                }

                fun onAdFailedToLoad(errorCode: Int) {
                    Log.d("dddd", "Ad failed to load! error code: $errorCode")
                }

                fun onAdLeftApplication() {
                    Log.d("dddd", "Ad left application!")
                }

                override fun onAdOpened() {
                    Log.d("dddd", "Ad is opened!")
                }
            }
        }
        binding.adView.addView(adView)
    }

    private fun initRecyclerView() {
        RecyclerUtils.setGridManager(this, binding.rcvHistory, adapter)
    }

    override fun initListener() {
        adapter.setOnClickItemRecyclerView { result, _ ->
            val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(RESULT, result)
            clipboard.setPrimaryClip(clip)
        }
        adapter.onClickRemoveListener = { position ->
            removeItem(position)
        }
        binding.toolbar.onLeftClickListener = {
            onBackPressed()
        }
        binding.toolbar.onRightClickListener = {
            clearData()
        }
    }

    private fun clearData() {
        AlertMessageDialog(this).also { dialog ->
            dialog.show(
                title = "Xác nhận xóa",
                message = "Bạn có chắc chắn muốn xóa toàn bộ lịch sử quét QR?",
                "Xóa",
                "Hủy",
                onClickSubmit = {
                    adapter.clearData()
                    LocalCache.getInstance().clearAll()
                }
            )
            dialog.setIconImageAlert(R.drawable.garbage)
        }
    }

    private fun removeItem(position: Int) {
        adapter.dataList.removeAt(position)
        adapter.notifyItemRemoved(position)
        LocalCache.getInstance().put(HISTORY, History().apply {
            listData = adapter.dataList
        })
    }

    override fun initData() {
        val list = LocalCache.getInstance().getParcelable(HISTORY, History::class.java)?.listData ?: mutableListOf()
        adapter.addData(list)
    }
}