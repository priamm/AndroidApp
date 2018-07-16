package com.enecuum.androidapp.ui.activity.testActivity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.enecuum.androidapp.R
import com.jakewharton.rxbinding2.widget.RxTextView.textChanges
import kotlinx.android.synthetic.main.test_activity.*

class TestActivity : Activity() {

    private val BN_PATH = "195.201.226.28"//"88.99.86.200"
    private val BN_PORT = "1554"
    private val NN_PATH = "195.201.226.26"//"195.201.226.30"//"195.201.226.25"
    private val NN_PORT = "1554"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity)

        val sharedPreferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
        val customNN = "customNetworkNode"
        val customBN = "customBootNode"
        val customBNPORT = "bootNodePort"
        val customBNIP = "bootNodeIp"
        val customNNPORT = "networkNodePort"
        val customNNIP = "networkNodeIp"



        bootNodePort.setText(sharedPreferences.getString(customBNPORT, BN_PORT))
        bootNodeIp.setText(sharedPreferences.getString(customBNIP, BN_PATH))
        networkNodePort.setText(sharedPreferences.getString(customNNPORT, NN_PORT))
        networkNodeIp.setText(sharedPreferences.getString(customNNIP, NN_PATH))

        textChanges(bootNodePort).subscribe { s -> sharedPreferences.edit { putString(customBNPORT, s.toString()) } }
        textChanges(bootNodeIp).subscribe { s -> sharedPreferences.edit { putString(customBNIP, s.toString()) } }
        textChanges(networkNodePort).subscribe { s -> sharedPreferences.edit { putString(customNNPORT, s.toString()) } }
        textChanges(networkNodeIp).subscribe { s -> sharedPreferences.edit { putString(customNNIP, s.toString()) } }

        cbNN.setOnCheckedChangeListener { buttonView, isChecked ->
            networkNodePort.isEnabled = isChecked
            networkNodeIp.isEnabled = isChecked
            sharedPreferences.edit { putBoolean(customNN, isChecked) }
        }


        cbBN.setOnCheckedChangeListener { buttonView, isChecked ->
            bootNodePort.isEnabled = isChecked
            bootNodeIp.isEnabled = isChecked
            sharedPreferences.edit { putBoolean(customBN, isChecked) }
        }

        val customNNEnabled = sharedPreferences.getBoolean(customNN, false)
        cbNN.isChecked = customNNEnabled
        val customBnEnabled = sharedPreferences.getBoolean(customBN, false)
        cbBN.isChecked = customBnEnabled

        networkNodePort.isEnabled = customNNEnabled
        networkNodeIp.isEnabled = customNNEnabled
        bootNodePort.isEnabled = customBnEnabled
        bootNodeIp.isEnabled = customBnEnabled

        connect.setOnClickListener {
            val teamSize = teamSize.text.toString()
            var poaService = PoaService(this,
                    if (cbBN.isChecked) bootNodeIp.text.toString() else BN_PATH,
                    if (cbBN.isChecked) bootNodePort.text.toString() else BN_PORT,
                    if (cbNN.isChecked) networkNodeIp.text.toString() else NN_PATH,
                    if (cbNN.isChecked) networkNodePort.text.toString() else NN_PORT,
                    teamSize.toInt(),
                    onTeamSize = object : PoaService.onTeamListener {
                        override fun onTeamSize(size: Int) {
                            team.text = "Team size: ${size.toString()}"
                        }
                    }
            )

            poaService.connectAs(myNumber.text.toString().toInt())
        }
    }
}
