package com.enecuum.androidapp.ui.fragment.new_account_qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.new_account_qr.NewAccountQrView
import com.enecuum.androidapp.presentation.presenter.new_account_qr.NewAccountQrPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import android.graphics.Bitmap
import android.graphics.Color
import base_ui_primitives.TitleFragment
import kotlinx.android.synthetic.main.fragment_new_account_qr.*
import utils.KeyboardUtils
import utils.QrUtils


class NewAccountQrFragment : TitleFragment(), NewAccountQrView {
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
        val qr = QrUtils.createCodeFrom("�r��P���q���[�1GE�E�x�J��;��K+`�{�7������cE��S�6")
        qrCode.setImageBitmap(qr)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if(isVisibleToUser) {
            KeyboardUtils.hideKeyboard(activity, activity?.currentFocus)
        }
    }

    override fun getTitle() = getString(R.string.backup_file)
}
