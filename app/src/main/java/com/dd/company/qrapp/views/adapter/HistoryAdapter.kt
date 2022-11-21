package com.dd.company.qrapp.views.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dd.company.qrapp.databinding.LayoutItemHistoryBinding

class HistoryAdapter(val dataList: MutableList<String>) : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>() {
    private lateinit var binding: LayoutItemHistoryBinding
    var onClickRemoveListener: ((position: Int) -> Unit)? = null

    var setOnClickItemRecyclerView: ((item: String, position: Int) -> Unit)? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bindData(item: String, position: Int) {
            binding.apply {
                tvStt.text = "${position + 1}."
                tvResult.text = item
            }
            binding.btnRemove.setOnClickListener {
                onClickRemoveListener?.invoke(position)
            }
            binding.root.setOnClickListener {
                setOnClickItemRecyclerView?.invoke(item, position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = LayoutItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(dataList[position], position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        dataList.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(newList: MutableList<String>) {
        dataList.clear()
        dataList.addAll(newList)
        notifyDataSetChanged()
    }
}