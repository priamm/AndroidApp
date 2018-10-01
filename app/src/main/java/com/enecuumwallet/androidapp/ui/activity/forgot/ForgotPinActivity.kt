package com.enecuumwallet.androidapp.ui.activity.forgot

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.presentation.presenter.forgot.ForgotPinPresenter
import com.enecuumwallet.androidapp.presentation.view.forgot.ForgotPinView
import com.enecuumwallet.androidapp.ui.adapters.RestorePinPagerAdapter
import com.enecuumwallet.androidapp.ui.base_ui_primitives.BackActivity
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
        val adapter = RestorePinPagerAdapter(supportFragmentManager)
        pager.adapter = adapter
        indicator.setViewPager(pager)
        next.setOnClickListener {
            presenter.onNextClick(pager.currentItem)
        }
        presenter.onCreate()
    }

    override fun changeButtonState(enable: Boolean) {
        next.isEnabled = enable
    }

    override fun moveNext() {
        pager.setCurrentItem(pager.currentItem+1, true)
        if(pager.currentItem == 2) {
            next.text = getString(R.string.change_pin)
        }
    }
}
