package com.enecuum.androidapp.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import baseActivities.BaseActivity

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.main.MainView
import com.enecuum.androidapp.presentation.presenter.main.MainPresenter

class MainActivity : BaseActivity(), MainView {
    companion object {
        const val TAG = "MainActivity"
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }

    @InjectPresenter
    lateinit var mMainPresenter: MainPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
