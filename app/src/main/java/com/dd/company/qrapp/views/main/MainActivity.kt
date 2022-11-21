package com.dd.company.qrapp.views.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dd.company.qrapp.R
import com.dd.company.qrapp.base.BaseActivity
import com.dd.company.qrapp.base.HISTORY
import com.dd.company.qrapp.base.RESULT
import com.dd.company.qrapp.databinding.ActivityMainBinding
import com.dd.company.qrapp.extensions.getAdSizeFollowScreen
import com.dd.company.qrapp.extensions.openAppSetting
import com.dd.company.qrapp.extensions.showDialogConfirm
import com.dd.company.qrapp.extensions.viewBinding
import com.dd.company.qrapp.model.History
import com.dd.company.qrapp.pref.LocalCache
import com.dd.company.qrapp.views.ResultActivity
import com.google.android.gms.ads.*
import com.google.zxing.*
import com.google.zxing.client.android.Intents
import com.google.zxing.common.HybridBinarizer
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import java.lang.RuntimeException

class MainActivity : BaseActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

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
                    getString(R.string.non_permission)
                ) {
                    openAppSetting()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
    }

    fun initView() {
        LocalCache.initialize(this)
        binding.barcodeView.setStatusText("")
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#2DC9BC")))
    }

    fun initListener() {
        initTestDevice()
        showAds()
        checkPermission()
    }

    private fun initTestDevice() {
        //First: see your log: Use RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()....
        //set up test device
//        val testDeviceIds = listOf("A9D3CCFDFE7EFAF324465227C5FA8E44")
//        val configuration = RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build()
//        MobileAds.setRequestConfiguration(configuration)
        MobileAds.initialize(this) {
        }
    }

    private fun showAds() {
        val adRequest = AdRequest.Builder()
            .build()
        if (binding.adView.childCount > 0) return
        val adView = AdView(this)
        adView.apply {
            adUnitId = "ca-app-pub-7304974533758848/5274950434"
            setAdSize(getAdSizeFollowScreen())
            loadAd(adRequest)
        }
        binding.adView.addView(adView)
    }

    private fun checkPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) -> {
                resumeBarcode()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    private fun resumeBarcode() {
        binding.barcodeView.let {
            it.pause()
            it.decodeSingle(object : BarcodeCallback {
                override fun barcodeResult(result: BarcodeResult?) {
                    saveToHistory(result?.text ?: "")
                    val intent = Intent(this@MainActivity, ResultActivity::class.java)
                    intent.putExtra(RESULT, result?.text ?: "")
                    startActivity(intent)
//                    Toast.makeText(this@MainActivity, "${result?.text}", Toast.LENGTH_SHORT).show()
                }

                override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
                    isInvertedScan = !isInvertedScan
                    mIntent.putExtra(Intents.Scan.INVERTED_SCAN, isInvertedScan)
                    it.initializeFromIntent(mIntent)
                }

            })
            it.resume()
        }
    }

    private fun saveToHistory(result: String) {
        val history =
            LocalCache.getInstance().getParcelable(HISTORY, History::class.java) ?: History()
        history.listData.add(0, result)
        LocalCache.getInstance().put(HISTORY, history)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.choose_image -> requestPickImage.launch("image/*")
            R.id.history -> startActivity(Intent(this,HistoryActivity::class.java))
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
            showDialogConfirm("", getString(R.string.image_has_no_qr), false) {}
        }
    }

}