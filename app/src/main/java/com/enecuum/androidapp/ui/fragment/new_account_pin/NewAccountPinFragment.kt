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
import utils.SimpleTextWatcher


class NewAccountPinFragment : MvpAppCompatFragment(), NewAccountPinView {
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
        showKeyboard()
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

    private fun showKeyboard() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            pin1.requestFocus()
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(pin1, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }
}
