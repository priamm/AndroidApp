package com.enecuum.androidapp.ui.fragment.new_account_pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.new_account_pin.NewAccountPinView
import com.enecuum.androidapp.presentation.presenter.new_account_pin.NewAccountPinPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_new_account_pin.*
import android.text.Editable
import android.widget.EditText
import com.enecuum.androidapp.ui.base_ui_primitives.SlideshowTitleFragment
import com.enecuum.androidapp.utils.KeyboardUtils
import com.enecuum.androidapp.utils.PinUtils
import com.enecuum.androidapp.utils.SimpleTextWatcher


class NewAccountPinFragment : SlideshowTitleFragment(), NewAccountPinView {
    companion object {
        const val TAG = "NewAccountPinFragment"

        fun newInstance(): NewAccountPinFragment {
            val fragment: NewAccountPinFragment = NewAccountPinFragment()
            val args: Bundle = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: NewAccountPinPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_new_account_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        KeyboardUtils.showKeyboard(activity, pin1)
        for(field in listOf<EditText>(pin1, pin2)) {
            field.addTextChangedListener(object : SimpleTextWatcher() {
                override fun afterTextChanged(s: Editable?) {
                    validateFields()
                }
            })
            field.onFocusChangeListener = KeyboardUtils.createMoveCursorToEndFocusListener()
        }
    }

    private fun validateFields() {
        presenter.validateFields(pin1.text.toString(), pin2.text.toString())
    }

    override fun getTitle() = getString(R.string.pin_creation)

    override fun refreshPinState(length: Int, pinString: NewAccountPinPresenter.PinString) {
        when(pinString) {
            NewAccountPinPresenter.PinString.First -> {
                PinUtils.changePinState(pin1_1, pin1_2, pin1_3, pin1_4, length)
            }
            NewAccountPinPresenter.PinString.Second -> {
                PinUtils.changePinState(pin2_1, pin2_2, pin2_3, pin2_4, length)
            }
        }
    }
}
