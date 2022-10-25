package com.dd.company.baseapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.dd.company.baseapp.base.BaseActivity
import com.dd.company.baseapp.databinding.ActivityMainBinding
import com.dd.company.baseapp.extensions.openAppSetting
import com.dd.company.baseapp.extensions.showAlertDialogConfirm
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CompoundBarcodeView

class MainActivity : BaseActivity<ActivityMainBinding>() {

    var mIntent: Intent = Intent()

    private var isInvertedScan = false


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                showAlertDialogConfirm(
                    "",
                    "Bạn chưa cấp quyền sử dụng máy ảnh, để sử dụng ứng dụng vui lòng bật quyền truy cập camera"
                ) {
                    openAppSetting()
                }
            }
        }

    override fun initView() {
        binding.barcodeView.setStatusText("")
    }

    override fun initListener() {
        checkPermission()
    }

    override fun initData() {

    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {

            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        resumeBarcode()
    }

    fun resumeBarcode() {
        binding.barcodeView.let {
            it.pause()
            it.decodeSingle(object : BarcodeCallback {
                override fun barcodeResult(result: BarcodeResult?) {
                    Toast.makeText(this@MainActivity, "${result?.text}", Toast.LENGTH_SHORT).show()
                }

                override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
                    isInvertedScan = !isInvertedScan
                    mIntent.putExtra(Intents.Scan.INVERTED_SCAN, isInvertedScan)
                    it.initializeFromIntent(intent)
                }

            })
            it.resume()
        }
    }
}