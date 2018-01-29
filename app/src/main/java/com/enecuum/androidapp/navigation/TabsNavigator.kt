package com.enecuum.androidapp.navigation

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.enecuum.androidapp.base_ui_primitives.HomeTab
import com.enecuum.androidapp.base_ui_primitives.ReceiveTab
import com.enecuum.androidapp.base_ui_primitives.SendTab
import com.enecuum.androidapp.base_ui_primitives.SettingsTab
import com.enecuum.androidapp.ui.fragment.balance.BalanceFragment
import com.enecuum.androidapp.ui.fragment.tokens_jettons.TokensAndJettonsFragment
import ru.terrakok.cicerone.android.SupportAppNavigator

/**
 * Created by oleg on 29.01.18.
 */
class TabsNavigator(activity: FragmentActivity?,
                    fragmentManager: FragmentManager?,
                    containerId: Int) : SupportAppNavigator(activity, fragmentManager, containerId) {
    override fun createActivityIntent(context: Context?, screenKey: String?, data: Any?): Intent? {
        return null
    }

    override fun createFragment(screenKey: String?, data: Any?): Fragment? {
        if(screenKey == null)
            return null
        val tabType = TabType.valueOf(screenKey)
        return when (tabType) {
            TabType.Home -> HomeTab()
            TabType.Send -> SendTab()
            TabType.Receive -> ReceiveTab()
            TabType.Settings -> SettingsTab()
        }
    }
}