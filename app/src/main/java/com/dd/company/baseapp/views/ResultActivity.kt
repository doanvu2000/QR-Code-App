package com.dd.company.baseapp.views

import com.dd.company.baseapp.base.BaseActivity
import com.dd.company.baseapp.databinding.ActivityResultBinding

class ResultActivity  : BaseActivity<ActivityResultBinding>(){
    override fun initView() {

    }

    override fun initListener() {
        binding.apply {
            btnCopy.setOnClickListener {  }
            btnOpenBrowser.setOnClickListener {  }
            btnShare.setOnClickListener {  }
        }
    }

    override fun initData() {

    }
}