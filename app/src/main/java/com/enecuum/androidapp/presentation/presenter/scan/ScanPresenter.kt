package com.enecuum.androidapp.presentation.presenter.scan

import android.os.Handler
import android.os.Looper
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.presentation.presenter.send_parameters.SendParametersPresenter
import com.enecuum.androidapp.presentation.view.scan.ScanView


@InjectViewState
class ScanPresenter : MvpPresenter<ScanView>() {
    fun onCreate() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            EnecuumApplication.cicerone().router.exitWithResult(SendParametersPresenter.SCAN_RESULT, "test")
        }, 1000)
    }

}
