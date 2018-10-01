package com.enecuumwallet.androidapp.presentation.view.tokens_single

import com.arellomobile.mvp.MvpView
import com.enecuumwallet.androidapp.models.TokenInfo
import com.enecuumwallet.androidapp.presentation.view.ButtonStateView
import com.enecuumwallet.androidapp.ui.fragment.tokens_single.TokensSingleFragment

interface TokensSingleView : ButtonStateView {
    fun setupWithMode(currentMode: TokensSingleFragment.Companion.Mode, listOf: List<TokenInfo>)
}
