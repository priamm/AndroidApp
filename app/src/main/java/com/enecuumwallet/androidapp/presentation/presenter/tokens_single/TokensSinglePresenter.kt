package com.enecuumwallet.androidapp.presentation.presenter.tokens_single

import android.os.Bundle
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuumwallet.androidapp.events.TokensSelected
import com.enecuumwallet.androidapp.models.TokenInfo
import com.enecuumwallet.androidapp.presentation.view.tokens_single.TokensSingleView
import com.enecuumwallet.androidapp.ui.fragment.tokens_single.TokensSingleFragment
import com.enecuumwallet.androidapp.ui.fragment.tokens_single.TokensSingleFragment.Companion.MODE
import com.enecuumwallet.androidapp.utils.EventBusUtils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@InjectViewState
class TokensSinglePresenter : MvpPresenter<TokensSingleView>() {
    fun handleArgs(arguments: Bundle?) {
        EventBusUtils.register(this)
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


    override fun onDestroy() {
        super.onDestroy()
        EventBusUtils.unregister(this)
    }

    @Subscribe
    fun onSelectedIdsListChanged(idList: TokensSelected) {
        viewState.changeButtonState(idList.list.isNotEmpty())
    }
}
