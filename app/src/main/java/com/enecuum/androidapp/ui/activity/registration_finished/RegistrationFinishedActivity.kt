package com.enecuum.androidapp.ui.activity.registration_finished

import android.content.Context
import android.content.Intent
import android.os.Bundle
import base_ui_primitives.BackActivity

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.registration_finished.RegistrationFinishedView
import com.enecuum.androidapp.presentation.presenter.registration_finished.RegistrationFinishedPresenter


class RegistrationFinishedActivity : BackActivity(), RegistrationFinishedView {
    companion object {
        const val TAG = "RegistrationFinishedActivity"
    }

    @InjectPresenter
    lateinit var presenter: RegistrationFinishedPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_finished)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}
