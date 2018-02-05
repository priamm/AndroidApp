package com.enecuum.androidapp.ui.activity.scan

import android.content.Context
import android.content.Intent
import android.os.Bundle

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.scan.ScanView
import com.enecuum.androidapp.presentation.presenter.scan.ScanPresenter

import com.enecuum.androidapp.ui.base_ui_primitives.BackActivity


class ScanActivity : BackActivity(), ScanView {
    companion object {
        const val TAG = "ScanActivity"
        fun getIntent(context: Context): Intent = Intent(context, ScanActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: ScanPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        presenter.onCreate()
    }
}
