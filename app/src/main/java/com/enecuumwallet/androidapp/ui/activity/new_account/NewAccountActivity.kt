package com.enecuumwallet.androidapp.ui.activity.new_account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.models.inherited.models.EcdsaKeyPairManager
import com.enecuumwallet.androidapp.presentation.presenter.new_account.NewAccountPresenter
import com.enecuumwallet.androidapp.presentation.view.new_account.NewAccountView
import com.enecuumwallet.androidapp.ui.adapters.NewAccountPagerAdapter
import com.enecuumwallet.androidapp.ui.base_ui_primitives.BackActivity
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
        title = getString(R.string.pin_creation)
    }

    override fun moveNext() {
        pager.setCurrentItem(pager.currentItem + 1, true)
        when (pager.currentItem) {
            1 -> {
                title = getString(R.string.pin_creation)
            }
//            2 -> {
//                title = getString(R.string.backup_file)
//            }
            2 -> {
                title = getString(R.string.seed_phrase)
            }
        }
    }

    override fun changeButtonState(enable: Boolean) {
        next.isEnabled = enable
    }

    override fun displaySkipDialog() {
        val dialog = AlertDialog.Builder(this)
                .setMessage(R.string.skip_dialog_message)
                .setPositiveButton(android.R.string.ok, presenter)
                .setNegativeButton(android.R.string.cancel, presenter)
        dialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }
}
