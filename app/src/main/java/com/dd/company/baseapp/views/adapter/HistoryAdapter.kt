package com.dd.company.baseapp.views.adapter

import android.annotation.SuppressLint
import com.dd.company.baseapp.databinding.LayoutItemHistoryBinding
import com.dd.company.baseapp.widget.recyclerview.BaseRecyclerViewAdapter

class HistoryAdapter : BaseRecyclerViewAdapter<String, LayoutItemHistoryBinding>() {
    var onClickRemoveListener: ((position: Int) -> Unit)? = null

    @SuppressLint("SetTextI18n")
    override fun bindData(binding: LayoutItemHistoryBinding, item: String, position: Int) {
        binding.apply {
            tvStt.text = "${position + 1}."
            tvResult.text = item
        }
        binding.btnRemove.setOnClickListener {
            onClickRemoveListener?.invoke(position)
        }
    }
}