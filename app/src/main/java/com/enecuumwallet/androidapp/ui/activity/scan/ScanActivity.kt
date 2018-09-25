package com.enecuumwallet.androidapp.ui.activity.scan

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.presentation.view.scan.ScanView
import com.enecuumwallet.androidapp.presentation.presenter.scan.ScanPresenter

import com.enecuumwallet.androidapp.ui.base_ui_primitives.BackActivity
import com.enecuumwallet.androidapp.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_scan.*


class ScanActivity : BackActivity(), ScanView {
    companion object {
        const val TAG = "ScanActivity"
        fun getIntent(context: Context): Intent = Intent(context, ScanActivity::class.java)
    }

    private var isPermissionGranted = false

    @InjectPresenter
    lateinit var presenter: ScanPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        presenter.setup(zxingScanner)
    }

    override fun onResume() {
        super.onResume()
        if(!isPermissionGranted)
            return
        zxingScanner.setResultHandler(presenter)
        zxingScanner.startCamera()
    }

    override fun requestPermissions() {
        PermissionUtils.requestPermissions(this, PermissionUtils.cameraPermissions)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionUtils.checkPermissionsAndRunFunction({ presenter.onPermissionsGranted()}, requestCode, grantResults)
    }

    override fun onPermissionsGranted() {
        isPermissionGranted = true
        onResume()
    }

    override fun onPause() {
        super.onPause()
        zxingScanner.stopCamera()
    }
}
