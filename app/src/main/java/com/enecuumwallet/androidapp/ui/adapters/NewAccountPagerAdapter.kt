package com.enecuumwallet.androidapp.ui.adapters

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.ui.fragment.create_seed.CreateSeedFragment
import com.enecuumwallet.androidapp.ui.fragment.new_account_qr.NewAccountQrFragment
import com.enecuumwallet.androidapp.ui.fragment.pin.PinFragment

/**
 * Created by oleg on 23.01.18.
 */
class NewAccountPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val args = Bundle()
        when(position) {
            0 -> {
                args.putString(PinFragment.TITLE, EnecuumApplication.applicationContext().getString(R.string.enter_new_pin))
                return PinFragment.newInstance(args)
            }
            1 -> {
                args.putString(PinFragment.TITLE, EnecuumApplication.applicationContext().getString(R.string.confirm_new_pin))
                return PinFragment.newInstance(args)
            }
//            2 -> {
//                return NewAccountQrFragment.newInstance()
//            }
            2 -> return CreateSeedFragment.newInstance(Bundle.EMPTY)

        }
        return Fragment()
    }

    override fun getCount(): Int = 3
}