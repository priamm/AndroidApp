package utils

import android.app.Activity
import com.enecuum.androidapp.R
import com.github.angads25.filepicker.controller.DialogSelectionListener
import com.github.angads25.filepicker.model.DialogConfigs
import com.github.angads25.filepicker.model.DialogProperties
import com.github.angads25.filepicker.view.FilePickerDialog

/**
 * Created by oleg on 23.01.18.
 */
object FileSystemUtils {
    const val PICK_FILE_REQUEST = 1024
    fun chooseDirectory(activity: Activity, listener : DialogSelectionListener) {
        val properties = DialogProperties()
        properties.selection_mode = DialogConfigs.SINGLE_MODE
        properties.selection_type = DialogConfigs.DIR_SELECT
        val dialog = FilePickerDialog(activity, properties)
        dialog.setTitle(activity.getString(R.string.select_dir))
        dialog.setDialogSelectionListener(listener)
        dialog.show()
    }
}