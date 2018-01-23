package com.enecuum.androidapp.ui.activity.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import baseActivities.BaseActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.presenter.registration.RegistrationPresenter
import com.enecuum.androidapp.presentation.view.registration.RegistrationView
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : BaseActivity(), RegistrationView {
    companion object {
        const val TAG = "RegistrationActivity"
        fun getIntent(context: Context): Intent = Intent(context, RegistrationActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: RegistrationPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        newAccount.setOnClickListener {
            presenter.newAccountClick()
        }
        signIn.setOnClickListener {
            presenter.signInClick()
        }
    }
}
