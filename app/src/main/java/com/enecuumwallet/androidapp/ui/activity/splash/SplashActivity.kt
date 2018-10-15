package com.enecuumwallet.androidapp.ui.activity.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.enecuumwallet.androidapp.ui.base_ui_primitives.BaseActivity

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.presentation.view.splash.SplashView
import com.enecuumwallet.androidapp.presentation.presenter.splash.SplashPresenter


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

        val crashed = getIntent().getBooleanExtra("crash", false)
        if (crashed) {
            Toast.makeText(this, "App restarted after crash", Toast.LENGTH_SHORT).show()
        }

        PersistentStorage.setAutoMiningStart(crashed)
    }
}
