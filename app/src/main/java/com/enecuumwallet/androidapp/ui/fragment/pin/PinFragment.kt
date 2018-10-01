package com.enecuumwallet.androidapp.ui.fragment.pin

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.arellomobile.mvp.MvpAppCompatFragment
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.presentation.view.pin.PinView
import com.enecuumwallet.androidapp.presentation.presenter.pin.PinPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuumwallet.androidapp.utils.PinUtils
import com.enecuumwallet.androidapp.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.fragment_pin.*

class PinFragment : MvpAppCompatFragment(), PinView {
    companion object {
        const val TAG = "PinFragment"
        const val TITLE = "TITLE"
        fun newInstance(args: Bundle): PinFragment {
            val fragment = PinFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: PinPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.handleArgs(arguments)
        pin1.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                presenter.onPinTextChanged(pin1.text.toString())
            }
        })
        pin1.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                return@setOnEditorActionListener presenter.onDonePressed()
            }
            true
        }
    }

    override fun displayPin(length: Int) {
        PinUtils.changePinState(pin1_1, pin1_2, pin1_3, pin1_4, length)
    }

    override fun setupWithTitle(title: String?) {
        caption.text = title
    }
}
