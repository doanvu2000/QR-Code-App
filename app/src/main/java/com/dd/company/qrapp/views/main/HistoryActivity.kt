package com.dd.company.qrapp.views.main

import android.content.Intent
import android.os.Bundle
import com.dd.company.qrapp.R
import com.dd.company.qrapp.base.BaseActivity
import com.dd.company.qrapp.base.HISTORY
import com.dd.company.qrapp.base.RESULT
import com.dd.company.qrapp.databinding.ActivityHistoryBinding
import com.dd.company.qrapp.extensions.getAdSizeFollowScreen
import com.dd.company.qrapp.extensions.viewBinding
import com.dd.company.qrapp.model.History
import com.dd.company.qrapp.pref.LocalCache
import com.dd.company.qrapp.views.ResultActivity
import com.dd.company.qrapp.views.adapter.HistoryAdapter
import com.dd.company.qrapp.widget.dialog.AlertMessageDialog
import com.dd.company.qrapp.widget.recyclerview.RecyclerUtils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class HistoryActivity : BaseActivity() {

    private val binding by viewBinding(ActivityHistoryBinding::inflate)

    private val adapter by lazy {
        HistoryAdapter(mutableListOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
        initListener()
    }

    private fun initView() {
        supportActionBar?.hide()
        initRecyclerView()
        binding.toolbar.setTitleColor(R.color.white)
        showAds()
    }

    private fun showAds() {
        val adRequest = AdRequest.Builder()
            .build()
        if (binding.adView.childCount > 0) return
        val adView = AdView(this)
        adView.apply {
            adUnitId = getString(R.string.ads_id)
            setAdSize(getAdSizeFollowScreen())
            loadAd(adRequest)
        }
        binding.adView.addView(adView)
    }

    private fun initRecyclerView() {
        RecyclerUtils.setGridManager(this, binding.rcvHistory, adapter)
    }

    private fun initListener() {
        adapter.setOnClickItemRecyclerView = { result, _ ->
            val intent = Intent(this@HistoryActivity, ResultActivity::class.java)
            intent.putExtra(RESULT, result)
            startActivity(intent)
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
                title = getString(R.string.delete_confirm),
                message = getString(R.string.delete_all_confirm),
                getString(R.string.delete),
                getString(R.string.text_cancel),
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

    fun initData() {
        val list = LocalCache.getInstance().getParcelable(HISTORY, History::class.java)?.listData
            ?: mutableListOf()
        adapter.addData(list)
    }
}