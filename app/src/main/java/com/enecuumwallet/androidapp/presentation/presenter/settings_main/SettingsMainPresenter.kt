package com.enecuumwallet.androidapp.presentation.presenter.settings_main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.application.EnecuumApplication
import com.enecuumwallet.androidapp.navigation.FragmentType
import com.enecuumwallet.androidapp.navigation.ScreenType
import com.enecuumwallet.androidapp.navigation.TabType
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.presentation.view.settings_main.SettingsMainView
import com.enecuumwallet.androidapp.ui.activity.testActivity.ECDSAchiper
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.intellij.lang.annotations.Flow
import timber.log.Timber

@InjectViewState
class SettingsMainPresenter : MvpPresenter<SettingsMainView>() {

    companion object {
        const val ERROR_CODE_KEY_INVALID = "error_code_key_invalid"
    }

    fun onChangePinClick() {
        EnecuumApplication.navigateToActivity(ScreenType.ChangePin)
    }

    fun onBackupActionClick() {
        EnecuumApplication.navigateToFragment(FragmentType.SettingsBackup, TabType.Settings)
    }

    fun onAboutAppClick() {
        EnecuumApplication.navigateToFragment(FragmentType.SettingsAboutApp, TabType.Settings)
    }

    fun onCustomBNAppClick() {
        EnecuumApplication.navigateToFragment(FragmentType.CustomBootNode, TabType.Settings)
    }

    fun onMyWalletClick() {
        EnecuumApplication.navigateToFragment(FragmentType.MyWallet, TabType.Settings)
    }

    fun onOTPclick() {
        val pulicKey = PersistentStorage.getWallet()

        val signFlowable  = Single.create<String>({ emitter ->
            val sign = ECDSAchiper.signDataHex(pulicKey.toByteArray())
            emitter.onSuccess(sign)
        })

        signFlowable
                .flatMapPublisher({sign ->
                    return@flatMapPublisher EnecuumApplication
                            .otpApi
                            .getOtpCode(pulicKey, sign)
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d(it.code)
                    viewState.onShowOTPcode(code = it.code ?: "")
                } , {
                    Timber.d(it)
                })
    }
}
