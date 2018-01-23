package com.enecuum.androidapp.ui.fragment.new_account_pin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.new_account_pin.NewAccountPinView
import com.enecuum.androidapp.presentation.presenter.new_account_pin.NewAccountPinPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_new_account_pin.*
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import base_ui_primitives.TitleFragment
import utils.KeyboardUtils
import utils.SimpleTextWatcher


class NewAccountPinFragment : TitleFragment(), NewAccountPinView {
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
        }
    }

    private fun validateFields() {
        presenter.validateFields(pin1.text.toString(), pin2.text.toString())
    }

    override fun getTitle() = getString(R.string.pin_creation)
}
