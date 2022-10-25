package com.dd.company.baseapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.dd.company.baseapp.base.BaseActivity
import com.dd.company.baseapp.base.RESULT
import com.dd.company.baseapp.databinding.ActivityMainBinding
import com.dd.company.baseapp.extensions.openAppSetting
import com.dd.company.baseapp.extensions.showAlertDialogConfirm
import com.dd.company.baseapp.pref.LocalCache
import com.dd.company.baseapp.views.ResultActivity
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult

class MainActivity : BaseActivity<ActivityMainBinding>() {

    var mIntent: Intent = Intent()
    private var isInvertedScan = false

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                showAlertDialogConfirm(
                    "",
                    getString(R.string.non_permission)
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
        LocalCache.initialize(this)
    }

    private fun checkPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) -> {

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

    private fun resumeBarcode() {
        binding.barcodeView.let {
            it.pause()
            it.decodeSingle(object : BarcodeCallback {
                override fun barcodeResult(result: BarcodeResult?) {
                    val intent = Intent(this@MainActivity, ResultActivity::class.java)
                    intent.putExtra(RESULT, result?.text ?: "")
                    startActivity(intent)
//                    Toast.makeText(this@MainActivity, "${result?.text}", Toast.LENGTH_SHORT).show()
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