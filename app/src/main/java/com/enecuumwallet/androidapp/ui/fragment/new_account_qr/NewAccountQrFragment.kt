package com.enecuumwallet.androidapp.ui.fragment.new_account_qr

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.ui.base_ui_primitives.FileOpeningFragment
import com.enecuumwallet.androidapp.presentation.presenter.new_account_qr.NewAccountQrPresenter
import com.enecuumwallet.androidapp.presentation.view.new_account_qr.NewAccountQrView
import kotlinx.android.synthetic.main.fragment_new_account_qr.*
import com.enecuumwallet.androidapp.utils.KeyboardUtils
import com.enecuumwallet.androidapp.utils.QrUtils


class NewAccountQrFragment : FileOpeningFragment(), NewAccountQrView {
    companion object {
        const val TAG = "NewAccountQrFragment"

        fun newInstance(): NewAccountQrFragment {
            val fragment: NewAccountQrFragment = NewAccountQrFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: NewAccountQrPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_account_qr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        save.setOnClickListener {
            presenter.onSaveClick()
        }
        copy.setOnClickListener {
            presenter.onCopyClick()
        }
        share.setOnClickListener {
            presenter.onShareClick()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser) {
            presenter.beginGenerateKeys()
            KeyboardUtils.hideKeyboard(activity, activity?.currentFocus)
        }
    }

    override fun showQrCode(key: String) {
        val qr = QrUtils.createCodeFrom(key)
        qrCode.setImageBitmap(qr)
        keyText.text = key
    }

    override fun getTitle() = getString(R.string.backup_file)

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun getFilePresenter(): FileOpeningPresenter = presenter
}
