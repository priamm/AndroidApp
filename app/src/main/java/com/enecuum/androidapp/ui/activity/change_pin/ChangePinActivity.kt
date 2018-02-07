package com.enecuum.androidapp.ui.activity.change_pin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.change_pin.ChangePinView
import com.enecuum.androidapp.presentation.presenter.change_pin.ChangePinPresenter
import com.enecuum.androidapp.ui.base_ui_primitives.BackActivity
import com.enecuum.androidapp.utils.KeyboardUtils
import com.enecuum.androidapp.utils.MiningUtils
import com.enecuum.androidapp.utils.PinUtils
import com.enecuum.androidapp.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.activity_change_pin.*
import kotlinx.android.synthetic.main.mining_toolbar.*


class ChangePinActivity : BackActivity(), ChangePinView {
    companion object {
        const val TAG = "ChangePinActivity"
        fun getIntent(context: Context): Intent = Intent(context, ChangePinActivity::class.java)
        enum class CurrentPhase {
            CurrentPin,
            NewPin,
            ConfirmPin
        }
    }

    @InjectPresenter
    lateinit var presenter: ChangePinPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_pin)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        KeyboardUtils.showKeyboard(this, pinText)
        presenter.onCreate()
        pinText.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                presenter.onPinTextChanged(pinText.text.toString())
            }
        })
        next.setOnClickListener {
            presenter.onNextClick()
        }
        MiningUtils.setupMiningPanel(miningStatusChanger, miningPanel, this)
    }

    override fun setupForPhase(currentPhase: CurrentPhase) {
        when(currentPhase) {
            ChangePinActivity.Companion.CurrentPhase.CurrentPin -> {
                setup(R.string.enter_current_pin, R.string.next)
            }
            ChangePinActivity.Companion.CurrentPhase.NewPin -> {
                setup(R.string.enter_new_pin, R.string.next)
            }
            ChangePinActivity.Companion.CurrentPhase.ConfirmPin -> {
                setup(R.string.confirm_new_pin, R.string.change_pin)
            }
        }
    }

    private fun setup(title: Int, buttonTitle: Int) {
        currentTitle.text = getString(title)
        next.text = getString(buttonTitle)
        pinText.setText("")
    }

    override fun displayPin(length: Int) {
        PinUtils.changePinState(pin1, pin2, pin3, pin4, length)
    }

    override fun changeButtonState(enable: Boolean) {
        next.isEnabled = enable
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }

    override fun setupMiningPanel() {
        MiningUtils.refreshMiningPanel(miningIcon, miningStatus)
    }

    override fun onResume() {
        super.onResume()
        setupMiningPanel()
    }
}
