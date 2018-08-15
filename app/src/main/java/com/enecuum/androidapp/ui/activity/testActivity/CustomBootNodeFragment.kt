package com.enecuum.androidapp.ui.activity.testActivity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuum.androidapp.R
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.ui.base_ui_primitives.BackTitleFragment
import com.jakewharton.rxbinding2.widget.RxTextView.textChanges
import com.jraska.console.Console
import kotlinx.android.synthetic.main.settings_fragment.*
import java.util.*

class CustomBootNodeFragment : BackTitleFragment() {
    override fun getTitle(): String {
        return activity!!.getString(R.string.custom_bn);
    }

    companion object {
        const val TAG = "CustomBootNodeFragment"
        val customBN = "customBootNode"
        val customBNPORT = "bootNodePort"
        val customBNIP = "bootNodeIp"

        val BN_PATH_DEFAULT = "212.92.98.141"//"88.99.86.200"
        val BN_PORT_DEFAULT = "1554"

        fun newInstance(): CustomBootNodeFragment {
            val fragment = CustomBootNodeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }
    val timer = Timer()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        timer.schedule(object :TimerTask(){
//            override fun run() {
//
//            }
//        },1000,30000)

        val sharedPreferences = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE);
        bootNodePort.setText(sharedPreferences?.getString(customBNPORT, BN_PORT_DEFAULT))
        bootNodeIp.setText(sharedPreferences?.getString(customBNIP, BN_PATH_DEFAULT))

        textChanges(bootNodePort).subscribe { s -> sharedPreferences?.edit { putString(customBNPORT, s.toString()) } }
        textChanges(bootNodeIp).subscribe { s -> sharedPreferences?.edit { putString(customBNIP, s.toString()) } }

        countTransactions.setText(PersistentStorage.getCountTransactionForRequest().toString())
        textChanges(countTransactions).filter { isInteger(it.toString()) }.subscribe { PersistentStorage.setCountTransactionForRequest(Integer.parseInt(it.toString())) }

        cbBN.setOnCheckedChangeListener { buttonView, isChecked ->
            bootNodePort.isEnabled = isChecked
            bootNodeIp.isEnabled = isChecked
            sharedPreferences?.edit { putBoolean(customBN, isChecked) }
        }

        val customBnEnabled = sharedPreferences?.getBoolean(customBN, false)
        cbBN.isChecked = customBnEnabled!!

        bootNodePort.isEnabled = customBnEnabled
        bootNodeIp.isEnabled = customBnEnabled

        restart.setOnClickListener {
            android.os.Process.killProcess(android.os.Process.myPid())
        }

        clean.setOnClickListener({
            Console.clear()
        })

    }

    fun isInteger(s: String): Boolean {
        return isInteger(s, 10)
    }

    fun isInteger(s: String, radix: Int): Boolean {
        if (s.isEmpty()) return false
        for (i in 0 until s.length) {
            if (i == 0 && s[i] == '-') {
                return if (s.length == 1)
                    false
                else
                    continue
            }
            if (Character.digit(s[i], radix) < 0) return false
        }
        return true
    }

    override fun onDestroyView() {
        timer.cancel()
        super.onDestroyView()
    }
}
