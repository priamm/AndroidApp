package com.enecuum.androidapp.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.util.DisplayMetrics
import com.enecuum.androidapp.application.EnecuumApplication


/**
 * Created by oleg on 23.01.18.
 */
object KeyboardUtils {
    fun showKeyboard(context: Context?, editText: EditText) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            editText.requestFocus()
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }

    fun hideKeyboard(context: Context?, view: View?) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }, 200)
    }

    fun createMoveCursorToEndFocusListener() : View.OnFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
        if(v != null && hasFocus) {
            val editText = v as EditText
            if(editText.text.isNotEmpty()) {
                editText.post({
                    editText.setSelection(editText.length())
                })
            }
        }
    }

    fun isKeyboardShown(rootView: View): Boolean {
        val softKeyboardHeight = 100
        val r = Rect()
        rootView.getWindowVisibleDisplayFrame(r)
        val dm = rootView.resources.displayMetrics
        val heightDiff = rootView.bottom - r.bottom
        return heightDiff > softKeyboardHeight * dm.density
    }

    fun copyToClipboard(value: String) {
        val clipboard = EnecuumApplication.applicationContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("enecuum key", value)
        clipboard.primaryClip = clip
    }
}