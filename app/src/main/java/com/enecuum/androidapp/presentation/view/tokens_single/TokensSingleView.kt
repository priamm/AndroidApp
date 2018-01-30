package com.enecuum.androidapp.presentation.view.tokens_single

import com.arellomobile.mvp.MvpView
import com.enecuum.androidapp.models.TokenInfo
import com.enecuum.androidapp.ui.fragment.tokens_single.TokensSingleFragment

interface TokensSingleView : MvpView {
    fun setupWithMode(currentMode: TokensSingleFragment.Companion.Mode, listOf: List<TokenInfo>)
}
