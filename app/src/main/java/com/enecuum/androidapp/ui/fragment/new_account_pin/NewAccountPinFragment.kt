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
import android.widget.ImageView
import com.enecuum.androidapp.base_ui_primitives.TitleFragment
import com.enecuum.androidapp.utils.KeyboardUtils
import com.enecuum.androidapp.utils.SimpleTextWatcher


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
            field.onFocusChangeListener = object : View.OnFocusChangeListener {
                override fun onFocusChange(v: View?, hasFocus: Boolean) {
                    if(v != null && hasFocus) {
                        val editText = v as EditText
                        if(!editText.text.isEmpty()) {
                            editText.post({
                                editText.setSelection(editText.length())
                            })
                        }
                    }
                }

            }
        }
    }

    private fun validateFields() {
        presenter.validateFields(pin1.text.toString(), pin2.text.toString())
    }

    override fun getTitle() = getString(R.string.pin_creation)

    override fun setPinVisible(visible: Boolean, pinString: NewAccountPinPresenter.PinString) {
        val visibility = if(visible) View.VISIBLE else View.INVISIBLE
        when(pinString) {
            NewAccountPinPresenter.PinString.First -> {
                pin1_1.visibility = visibility
                pin1_2.visibility = visibility
                pin1_3.visibility = visibility
                pin1_4.visibility = visibility
            }
            NewAccountPinPresenter.PinString.Second -> {
                pin2_1.visibility = visibility
                pin2_2.visibility = visibility
                pin2_3.visibility = visibility
                pin2_4.visibility = visibility
            }
        }
    }

    override fun refreshPinState(length: Int, pinString: NewAccountPinPresenter.PinString) {
        when(pinString) {
            NewAccountPinPresenter.PinString.First -> {
                changeDotState(length, 0, pin1_1)
                changeDotState(length, 1, pin1_2)
                changeDotState(length, 2, pin1_3)
                changeDotState(length, 3, pin1_4)
            }
            NewAccountPinPresenter.PinString.Second -> {
                changeDotState(length, 0, pin2_1)
                changeDotState(length, 1, pin2_2)
                changeDotState(length, 2, pin2_3)
                changeDotState(length, 3, pin2_4)
            }
        }
    }

    private fun changeDotState(length: Int, value2check : Int, dot: ImageView) {
        if(length > value2check)
            dot.setImageResource(R.drawable.dot_1)
        else
            dot.setImageResource(R.drawable.dot_2)
    }
}
