package com.enecuum.androidapp.ui.activity.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.enecuum.androidapp.base_ui_primitives.BaseActivity

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.splash.SplashView
import com.enecuum.androidapp.presentation.presenter.splash.SplashPresenter
import kotlinx.android.synthetic.main.activity_splash.*


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
        loading.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate))
        presenter.onCreate()
    }
}
