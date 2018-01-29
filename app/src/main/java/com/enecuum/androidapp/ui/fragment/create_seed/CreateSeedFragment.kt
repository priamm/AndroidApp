package com.enecuum.androidapp.ui.fragment.create_seed

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.enecuum.androidapp.R
import com.enecuum.androidapp.presentation.view.create_seed.CreateSeedView
import com.enecuum.androidapp.presentation.presenter.create_seed.CreateSeedPresenter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.enecuum.androidapp.base_ui_primitives.FileOpeningFragment
import com.enecuum.androidapp.utils.SeedUtils
import com.enecuum.androidapp.utils.SimpleTextWatcher
import kotlinx.android.synthetic.main.fragment_create_seed.*

class CreateSeedFragment : FileOpeningFragment(), CreateSeedView {
    companion object {
        const val TAG = "CreateSeedFragment"

        fun newInstance(): CreateSeedFragment {
            val fragment: CreateSeedFragment = CreateSeedFragment()
            val args: Bundle = Bundle()
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
        seed.addTextChangedListener(object : SimpleTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                presenter.validateSeed(seed.text.toString())
            }
        })
        save.setOnClickListener {
            presenter.onSaveClick()
        }
    }

    override fun getTitle(): String = getString(R.string.create_seed_title)

    override fun displayRemainWords(size: Int) {
        SeedUtils.displayRemainingCount(size, seedHint)
    }

    override fun setButtonEnabled(enabled: Boolean) {
        save.isEnabled = enabled
    }

    override fun getFilePresenter(): FileOpeningPresenter = presenter
}
