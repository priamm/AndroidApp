package com.enecuum.androidapp.presentation.presenter.new_account_qr

import android.content.*
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.presentation.view.new_account_qr.NewAccountQrView
import com.github.angads25.filepicker.controller.DialogSelectionListener
import com.enecuum.androidapp.persistent_data.PersistentStorage
import com.enecuum.androidapp.utils.PermissionUtils
import com.enecuum.androidapp.R
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.base_ui_primitives.FileOpeningFragment
import com.enecuum.androidapp.events.PinBackupFinished
import com.enecuum.androidapp.utils.FileSystemUtils
import org.greenrobot.eventbus.EventBus
import java.security.*


@InjectViewState
class NewAccountQrPresenter : MvpPresenter<NewAccountQrView>(), FileOpeningFragment.FileOpeningPresenter {
    override fun onSelectedFilePaths(files: Array<out String>?) {
        if(files != null && files.isNotEmpty()) {
            PersistentStorage.setKeyPath(files[0])
            notifyBackupFinished()
            //TODO: save keypair here
        }
    }

    private var isGenerationStarted = false
    private var keys : KeyPair? = null
    private var isSendingStarted = false

    fun beginGenerateKeys() {
        if(isGenerationStarted)
            return
        isGenerationStarted = true
        //TODO: get key from blockchain
        val generator = KeyPairGenerator.getInstance("RSA")
        val secureRandom = SecureRandom.getInstance("SHA1PRNG")
        generator.initialize(512, secureRandom)
        keys = generator.generateKeyPair()
        if(keys != null)
            viewState.showQrCode(keys?.public?.encoded.toString())
    }

    fun onSaveClick() {
        FileSystemUtils.checkPermissionsAndChooseDir(viewState)
    }

    override fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        PermissionUtils.checkPermissionsAndRunFunction({onSaveClick()}, requestCode, grantResults)
    }

    fun onCopyClick() {
        if(keys == null)
            return
        val clipboard = EnecuumApplication.applicationContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("public key", keys?.public?.encoded.toString())
        clipboard.primaryClip = clip
        EnecuumApplication.cicerone().router.showSystemMessage(EnecuumApplication.applicationContext().getString(R.string.key_copied_to_clipboard))
        notifyBackupFinished()
    }

    fun onShareClick() {
        if(keys == null)
            return
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, keys?.public?.encoded.toString())
        intent.type = "text/plain"
        viewState.sendKey(intent)
        isSendingStarted = true
    }

    fun onResume() {
        if(!isSendingStarted)
            return
        EnecuumApplication.cicerone().router.showSystemMessage(EnecuumApplication.applicationContext().getString(R.string.sharing_key_finished))
        notifyBackupFinished()
        isSendingStarted = false
    }

    private fun notifyBackupFinished() {
        EventBus.getDefault().post(PinBackupFinished())
    }

}
