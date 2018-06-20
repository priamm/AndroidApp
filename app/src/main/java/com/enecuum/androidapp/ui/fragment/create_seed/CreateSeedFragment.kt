package com.enecuum.androidapp.ui.fragment.create_seed

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.presenter.create_seed.CreateSeedPresenter
import com.enecuum.androidapp.presentation.view.create_seed.CreateSeedView
import com.enecuum.androidapp.ui.base_ui_primitives.FileOpeningFragment
import com.enecuum.androidapp.utils.SeedUtils
import com.enecuum.androidapp.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.fragment_create_seed.*
import io.github.novacrypto.bip39.wordlists.English
import io.github.novacrypto.bip39.SeedCalculator



class CreateSeedFragment : FileOpeningFragment(), CreateSeedView {
    companion object {
        const val TAG = "CreateSeedFragment"
        const val RESTORE_MODE = "RESTORE_MODE"
        fun newInstance(args: Bundle): CreateSeedFragment {
            val fragment: CreateSeedFragment = CreateSeedFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: CreateSeedPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_create_seed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val generateMnemonic = SeedUtils.generateMnemonic()


        seed.setText(generateMnemonic)
        seed.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                presenter.validateSeed(seed.text.toString())
            }
        })
        seed.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                return@setOnEditorActionListener presenter.onDonePressed()
            }
            true
        }
        save.setOnClickListener {
            presenter.onSaveClick()
        }
        presenter.handleArgs(arguments)
    }

    override fun getTitle(): String = getString(R.string.create_seed_title)

    override fun displayRemainWords(size: Int) {
        SeedUtils.displayRemainingCount(size, seedHint)
    }

    override fun setButtonEnabled(enabled: Boolean) {
        save.isEnabled = enabled
    }

    override fun getFilePresenter(): FileOpeningPresenter = presenter

    override fun setupRestoreMode() {
        save.visibility = View.GONE
        caption.text = getString(R.string.enter_your_seed_phrase)
    }
}
