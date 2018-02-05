package com.enecuum.androidapp.presentation.presenter.scan

import android.os.Build
import android.os.Handler
import android.os.Looper
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.presentation.presenter.send_parameters.SendParametersPresenter
import com.enecuum.androidapp.presentation.view.scan.ScanView
import com.enecuum.androidapp.utils.PermissionUtils
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView


@InjectViewState
class ScanPresenter : MvpPresenter<ScanView>(), ZXingScannerView.ResultHandler {
    override fun handleResult(p0: Result?) {
        if(p0 != null) {
            EnecuumApplication.cicerone().router.exitWithResult(SendParametersPresenter.SCAN_RESULT, p0.text)
        }
    }

    fun setup(scannerView : ZXingScannerView) {
        if(Build.MANUFACTURER.toLowerCase().contains("huawei")) {
            scannerView.setAspectTolerance(0.5f)
        }
        scannerView.setFormats(listOf(BarcodeFormat.QR_CODE))
        if(!PermissionUtils.checkPermissions(PermissionUtils.cameraPermissions)) {
            viewState.requestPermissions()
        } else {
            viewState.onPermissionsGranted()
        }
    }

    fun onPermissionsGranted() {
        viewState.onPermissionsGranted()
    }

}
