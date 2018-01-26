package com.enecuum.androidapp.ui.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.enecuum.androidapp.ui.fragment.create_seed.CreateSeedFragment
import com.enecuum.androidapp.ui.fragment.new_account_pin.NewAccountPinFragment
import com.enecuum.androidapp.ui.fragment.new_account_qr.NewAccountQrFragment

/**
 * Created by oleg on 23.01.18.
 */
class NewAccountPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> return NewAccountPinFragment()
            1 -> return NewAccountQrFragment()
            2 -> return CreateSeedFragment()
        }
        return Fragment()
    }

    override fun getCount(): Int = 3
}