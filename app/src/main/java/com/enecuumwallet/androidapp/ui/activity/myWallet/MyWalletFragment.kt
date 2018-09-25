package com.enecuumwallet.androidapp.ui.activity.myWallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.ui.activity.testActivity.Base58
import com.enecuumwallet.androidapp.ui.base_ui_primitives.BackTitleFragment
import kotlinx.android.synthetic.main.my_wallet_fragment.*

class MyWalletFragment : BackTitleFragment() {
    override fun getTitle(): String {
        return activity!!.getString(R.string.my_wallet);
    }

    companion object {
        const val TAG = "MyWalletFragment"
        fun newInstance(): MyWalletFragment {
            val fragment = MyWalletFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.my_wallet_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val address = PersistentStorage.getWallet()
        myId.setText(address)

    }
}
