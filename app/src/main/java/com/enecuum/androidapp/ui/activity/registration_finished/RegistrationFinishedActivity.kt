package com.enecuum.androidapp.ui.activity.registration_finished

import android.os.Bundle
import com.enecuum.androidapp.base_ui_primitives.BackActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.presenter.registration_finished.RegistrationFinishedPresenter
import com.enecuum.androidapp.presentation.view.registration_finished.RegistrationFinishedView
import kotlinx.android.synthetic.main.activity_registration_finished.*
import kotlinx.android.synthetic.main.transparent_toolbar.*


class RegistrationFinishedActivity : BackActivity(), RegistrationFinishedView {
    companion object {
        const val TAG = "RegistrationFinishedActivity"
    }

    @InjectPresenter
    lateinit var presenter: RegistrationFinishedPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration_finished)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        next.setOnClickListener {
            presenter.onNextButtonClick()
        }
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}
