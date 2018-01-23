package com.enecuum.androidapp.ui.activity.signin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import baseActivities.BackActivity

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.signin.SignInView
import com.enecuum.androidapp.presentation.presenter.signin.SignInPresenter


class SignInActivity : BackActivity(), SignInView {
    companion object {
        const val TAG = "SignInActivity"
        fun getIntent(context: Context): Intent = Intent(context, SignInActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: SignInPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }
}
