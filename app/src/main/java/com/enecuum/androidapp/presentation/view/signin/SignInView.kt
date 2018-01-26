package com.enecuum.androidapp.presentation.view.signin

import com.arellomobile.mvp.MvpView

interface SignInView : MvpView {
    fun displayPin(length: Int)
    fun changeButtonState(enabled: Boolean)

}
