package com.enecuum.androidapp.presentation.presenter.tokens_single

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.models.TokenInfo
import com.enecuum.androidapp.presentation.view.tokens_single.TokensSingleView
import com.enecuum.androidapp.ui.fragment.tokens_single.TokensSingleFragment
import com.enecuum.androidapp.ui.fragment.tokens_single.TokensSingleFragment.Companion.MODE

@InjectViewState
class TokensSinglePresenter : MvpPresenter<TokensSingleView>() {
    fun handleArgs(arguments: Bundle?) {
        val currentMode = arguments?.getSerializable(MODE) as TokensSingleFragment.Companion.Mode
        val tokenInfo = listOf<TokenInfo>(
                TokenInfo("DASH", 8.9, "0"),
                TokenInfo("BCH", 8.9, "1"),
                TokenInfo("IPP", 8.9, "2"),
                TokenInfo("ETHB", 8.9, "3")
        )
        viewState.setupWithMode(currentMode, tokenInfo)
    }

    fun onSendClick() {

    }

    fun onReceiveClick() {

    }

}
