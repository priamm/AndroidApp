package com.enecuum.androidapp.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.enecuum.androidapp.ui.fragment.balance.BalanceFragment
import com.enecuum.androidapp.ui.fragment.send_finish.SendFinishFragment
import com.enecuum.androidapp.ui.fragment.send_parameters.SendParametersFragment
import com.enecuum.androidapp.ui.fragment.tokens_jettons.TokensAndJettonsFragment
import ru.terrakok.cicerone.android.SupportAppNavigator

/**
 * Created by oleg on 29.01.18.
 */
class FragmentNavigator(activity: FragmentActivity?,
                        fragmentManager: FragmentManager?,
                        containerId: Int) : SupportAppNavigator(activity, fragmentManager, containerId) {
    override fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? {
        return null
    }

    override fun createFragment(screenKey: String?, data: Any?): Fragment? {
        if(screenKey == null)
            return null
        val fragmentType = FragmentType.valueOf(screenKey)
        when (fragmentType) {
            FragmentType.Balance -> return BalanceFragment.newInstance()
            FragmentType.Tokens -> return TokensAndJettonsFragment.newInstance()
            FragmentType.MainReceive -> TODO()
            FragmentType.QrReceive -> TODO()
            FragmentType.SendOptions -> return SendParametersFragment.newInstance()
            FragmentType.SendFinish -> return SendFinishFragment.newInstance(data as Bundle)
            FragmentType.Settings -> TODO()
        }
    }
}