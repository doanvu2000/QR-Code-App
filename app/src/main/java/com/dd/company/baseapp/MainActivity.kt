package com.dd.company.baseapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.dd.company.baseapp.base.BaseActivity
import com.dd.company.baseapp.databinding.ActivityMainBinding
import com.dd.company.baseapp.extensions.openAppSetting
import com.dd.company.baseapp.extensions.showDialogConfirm
import com.google.zxing.*
import com.google.zxing.client.android.Intents
import com.google.zxing.common.HybridBinarizer
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult

class MainActivity : BaseActivity<ActivityMainBinding>() {

    var mIntent: Intent = Intent()

    private var isInvertedScan = false

    private val requestPickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let {
            handleImagePicked(it)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                showDialogConfirm(
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.choose_image -> requestPickImage.launch("image/*")
            R.id.history -> Toast.makeText(this, "Show History Screen", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleImagePicked(imageUri: Uri) {
        try {
            val imageStream = contentResolver.openInputStream(imageUri)
            val selectedImage = BitmapFactory.decodeStream(imageStream)
            val intArray = IntArray(selectedImage.width * selectedImage.height)
            selectedImage.getPixels(
                intArray,
                0,
                selectedImage.width,
                0,
                0,
                selectedImage.width,
                selectedImage.height
            )
            val source: LuminanceSource =
                RGBLuminanceSource(selectedImage.width, selectedImage.height, intArray)
            val bitmap = BinaryBitmap(HybridBinarizer(source))
            val reader: Reader = MultiFormatReader()
            val result = reader.decode(bitmap)
            Toast.makeText(this, "${result.text}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            showAlertDialogConfirm("", getString(R.string.image_has_no_qr)) {}
        }
    }


}