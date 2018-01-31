package com.enecuum.androidapp.ui.activity.restore_pin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.ui.base_ui_primitives.BackActivity
import com.enecuum.androidapp.presentation.presenter.restore_pin.RestorePinPresenter
import com.enecuum.androidapp.presentation.view.restore_pin.RestorePinView
import kotlinx.android.synthetic.main.activity_restore_pin.*


class RestorePinActivity : BackActivity(), RestorePinView {
    companion object {
        const val TAG = "RestorePinActivity"
        fun getIntent(context: Context): Intent = Intent(context, RestorePinActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: RestorePinPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restore_pin)
        presenter.onCreate()
        next.setOnClickListener {
            presenter.onNextClick()
        }
    }

    override fun changeButtonState(enable: Boolean) {
        next.isEnabled = enable
    }
}
