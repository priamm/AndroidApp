package com.enecuum.androidapp.ui.activity.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import baseActivities.BaseActivity

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.splash.SplashView
import com.enecuum.androidapp.presentation.presenter.splash.SplashPresenter


class SplashActivity : BaseActivity(), SplashView {
    companion object {
        const val TAG = "SplashActivity"
        fun getIntent(context: Context): Intent = Intent(context, SplashActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: SplashPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        presenter.onCreate()
    }
}
