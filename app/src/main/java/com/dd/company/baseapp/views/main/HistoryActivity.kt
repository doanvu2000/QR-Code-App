package com.dd.company.baseapp.views.main

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.dd.company.baseapp.R
import com.dd.company.baseapp.base.BaseActivity
import com.dd.company.baseapp.base.HISTORY
import com.dd.company.baseapp.base.RESULT
import com.dd.company.baseapp.databinding.ActivityHistoryBinding
import com.dd.company.baseapp.model.History
import com.dd.company.baseapp.pref.LocalCache
import com.dd.company.baseapp.utils.pushParcelableBundle
import com.dd.company.baseapp.views.adapter.HistoryAdapter
import com.dd.company.baseapp.widget.dialog.AlertMessageDialog
import com.dd.company.baseapp.widget.recyclerview.RecyclerUtils

class HistoryActivity : BaseActivity<ActivityHistoryBinding>() {

    private val adapter by lazy {
        HistoryAdapter()
    }

    override fun initView() {
        supportActionBar?.hide()
        initRecyclerView()
        binding.toolbar.setTitleColor(R.color.white)
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