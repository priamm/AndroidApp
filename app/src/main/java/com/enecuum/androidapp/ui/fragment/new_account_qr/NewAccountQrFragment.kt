package com.enecuum.androidapp.ui.fragment.new_account_qr

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuum.androidapp.base_ui_primitives.TitleFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.presenter.new_account_qr.NewAccountQrPresenter
import com.enecuum.androidapp.presentation.view.new_account_qr.NewAccountQrView
import kotlinx.android.synthetic.main.fragment_new_account_qr.*
import com.enecuum.androidapp.utils.FileSystemUtils
import com.enecuum.androidapp.utils.KeyboardUtils
import com.enecuum.androidapp.utils.PermissionUtils
import com.enecuum.androidapp.utils.QrUtils


class NewAccountQrFragment : TitleFragment(), NewAccountQrView {
    companion object {
        const val QR_CODE_SIZE = 206f
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
        val qr = QrUtils.createCodeFrom(key, QR_CODE_SIZE, QR_CODE_SIZE)
        qrCode.setImageBitmap(qr)
        keyText.text = key
    }

    override fun getTitle() = getString(R.string.backup_file)

    override fun requestPermissions() {
        PermissionUtils.requestPermissions(this, PermissionUtils.storagePermissions)
    }

    override fun beginSelectKeyPath() {
        if(activity != null)
            FileSystemUtils.chooseDirectory(activity!!, presenter)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun sendKey(intent: Intent) {
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }
}
