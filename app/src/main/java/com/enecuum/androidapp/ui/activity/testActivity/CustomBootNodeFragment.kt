package com.enecuum.androidapp.ui.activity.testActivity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuum.androidapp.R
import com.enecuum.androidapp.ui.base_ui_primitives.BackTitleFragment
import com.jakewharton.rxbinding2.widget.RxTextView.textChanges
import kotlinx.android.synthetic.main.settings_fragment.*

class CustomBootNodeFragment : BackTitleFragment() {
    override fun getTitle(): String {
        return activity!!.getString(R.string.custom_bn);
    }

    companion object {
        const val TAG = "CustomBootNodeFragment"
        val customBN = "customBootNode"
        val customBNPORT = "bootNodePort"
        val customBNIP = "bootNodeIp"

        val BN_PATH_DEFAULT = "195.201.226.28"//"88.99.86.200"
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = activity?.getSharedPreferences("pref", Context.MODE_PRIVATE);
        bootNodePort.setText(sharedPreferences?.getString(customBNPORT, BN_PORT_DEFAULT))
        bootNodeIp.setText(sharedPreferences?.getString(customBNIP, BN_PATH_DEFAULT))

        textChanges(bootNodePort).subscribe { s -> sharedPreferences?.edit { putString(customBNPORT, s.toString()) } }
        textChanges(bootNodeIp).subscribe { s -> sharedPreferences?.edit { putString(customBNIP, s.toString()) } }


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

    }
}
