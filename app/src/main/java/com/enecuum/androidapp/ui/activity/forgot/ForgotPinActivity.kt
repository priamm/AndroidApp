package com.enecuum.androidapp.ui.activity.forgot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.ui.base_ui_primitives.BackActivity
import com.enecuum.androidapp.presentation.presenter.forgot.ForgotPinPresenter
import com.enecuum.androidapp.presentation.view.forgot.ForgotPinView
import com.enecuum.androidapp.utils.KeyboardUtils
import com.enecuum.androidapp.utils.SeedUtils
import com.enecuum.androidapp.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_forgot_pin.*


class ForgotPinActivity : BackActivity(), ForgotPinView {
    companion object {
        const val TAG = "ForgotPinActivity"
        fun getIntent(context: Context): Intent = Intent(context, ForgotPinActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: ForgotPinPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pin)
        KeyboardUtils.showKeyboard(this, seed)
        seed.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                presenter.onSeedTextChanged(seed.text.toString())
            }
        })
        next.setOnClickListener {
            presenter.onNextButtonClick()
        }
    }

    override fun setButtonEnabled(enabled: Boolean) {
        next.isEnabled = enabled
    }

    override fun displayRemainWords(size: Int) {
        SeedUtils.displayRemainingCount(size, seedHint)
    }
}
