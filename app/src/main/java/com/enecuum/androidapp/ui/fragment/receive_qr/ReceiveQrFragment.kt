package com.enecuum.androidapp.ui.fragment.receive_qr


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.presenter.receive_qr.ReceiveQrPresenter
import com.enecuum.androidapp.presentation.view.receive_qr.ReceiveQrView
import com.enecuum.androidapp.ui.base_ui_primitives.BackTitleFragment
import com.enecuum.androidapp.utils.QrUtils
import kotlinx.android.synthetic.main.fragment_receive_qr.*

class ReceiveQrFragment : BackTitleFragment(), ReceiveQrView {
    companion object {
        const val TAG = "ReceiveQrFragment"
        const val ADDRESS = "Address"
        fun newInstance(args: Bundle): ReceiveQrFragment {
            val fragment = ReceiveQrFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: ReceiveQrPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_receive_qr, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.handleArgs(arguments)
        setHasOptionsMenu(true)
    }

    override fun getTitle(): String = getString(R.string.use_qr_code)

    override fun setupWithAddress(address: String) {
        addressText.text = address
        val qr = QrUtils.createCodeFrom(address)
        qrCode.setImageBitmap(qr)
        copy.setOnClickListener {
            presenter.onCopyClick()
        }
    }

    override fun onResume() {
        super.onResume()
        menu?.clear()
    }
}
