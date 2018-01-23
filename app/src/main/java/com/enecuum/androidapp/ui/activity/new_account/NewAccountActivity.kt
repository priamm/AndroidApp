package com.enecuum.androidapp.ui.activity.new_account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import baseActivities.BackActivity

import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.new_account.NewAccountView
import com.enecuum.androidapp.presentation.presenter.new_account.NewAccountPresenter
import com.enecuum.androidapp.ui.adapters.NewAccountPagerAdapter
import kotlinx.android.synthetic.main.activity_new_account.*

class NewAccountActivity : BackActivity(), NewAccountView {
    companion object {
        const val TAG = "NewAccountActivity"
        fun getIntent(context: Context): Intent = Intent(context, NewAccountActivity::class.java)
    }

    @InjectPresenter
    lateinit var presenter: NewAccountPresenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate()
        setContentView(R.layout.activity_new_account)
        val adapter = NewAccountPagerAdapter(supportFragmentManager)
        pager.adapter = adapter
        indicator.setViewPager(pager)
        next.setOnClickListener {
            presenter.onNextClick(pager.currentItem)
        }
    }

    override fun openNextScreen() {
        pager.setCurrentItem(pager.currentItem+1, true)
    }

    override fun changeButtonState(enable: Boolean) {
        next.isEnabled = enable
    }
}
