package com.enecuumwallet.androidapp.ui.activity.testActivity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.persistent_data.PersistentStorage
import com.enecuumwallet.androidapp.ui.base_ui_primitives.BackTitleFragment
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
        val customTM = "customTeamNode"

        val customBNPORT = "bootNodePort"
        val customTNPORT = "teamNodePort"

        val customBNIP = "bootNodeIp"
        val customTNIP = "teamNodeIp"


        //val BN_PATH_DEFAULT = "staging.enecuum.com" //genesis-bootstrap.enecuum.com:1554
        val BN_PATH_DEFAULT = "genesis-bootstrap.enecuum.com"
        val BN_PORT_DEFAULT = "1554"                //

        //val TN_PATH_DEFAULT = "staging.enecuum.com:1557" //genesis-poa-teams.enecuum.com:8080
        val TN_PATH_DEFAULT = "genesis-poa-teams.enecuum.com"
        val TN_PORT_DEFAULT = "1557"               //1557

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

        et_tn_port.setText(sharedPreferences?.getString(customTNPORT, TN_PORT_DEFAULT))
        et_tn.setText(sharedPreferences?.getString(customTNIP, TN_PATH_DEFAULT))

        textChanges(et_tn_port).subscribe { s -> sharedPreferences?.edit { putString(customTNPORT, s.toString()) } }
        textChanges(et_tn).subscribe { s -> sharedPreferences?.edit { putString(customTNIP, s.toString()) } }

        countTransactions.setText(PersistentStorage.getCountTransactionForRequest().toString())
        textChanges(countTransactions).filter { isInteger(it.toString()) }.subscribe { PersistentStorage.setCountTransactionForRequest(Integer.parseInt(it.toString())) }

        cbBN.setOnCheckedChangeListener { buttonView, isChecked ->
            bootNodePort.isEnabled = isChecked
            bootNodeIp.isEnabled = isChecked
            sharedPreferences?.edit { putBoolean(customBN, isChecked) }
        }

        cb_tn.setOnCheckedChangeListener { btn, isChecked ->
            et_tn.isEnabled = isChecked
            et_tn_port.isEnabled = isChecked
            sharedPreferences?.edit { putBoolean(customTM, isChecked) }
        }

        val customBnEnabled = sharedPreferences?.getBoolean(customBN, false)
        cbBN.isChecked = customBnEnabled!!

        bootNodePort.isEnabled = customBnEnabled
        bootNodeIp.isEnabled = customBnEnabled

        val customTnEnabled = sharedPreferences.getBoolean(customTM, false)
        cb_tn.isChecked = customTnEnabled

        et_tn.isEnabled = customBnEnabled
        et_tn_port.isEnabled = customBnEnabled


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
