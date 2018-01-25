package com.enecuum.androidapp.presentation.view.new_account_pin

import com.arellomobile.mvp.MvpView
import com.enecuum.androidapp.presentation.presenter.new_account_pin.NewAccountPinPresenter

interface NewAccountPinView : MvpView {
    fun setPinVisible(visible: Boolean, pinString: NewAccountPinPresenter.PinString)
    fun refreshPinState(length: Int, pinString: NewAccountPinPresenter.PinString)

}
