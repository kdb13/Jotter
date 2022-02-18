package com.kdb.jotter.ui

import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Clears focus from this view when user presses the Back key.
 */
fun View.clearFocusOnBack() {
    setOnKeyListener { v, keyCode, _ ->
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            v.clearFocus()
            true
        } else false
    }
}

/**
 * Shows a keyboard for this view, if focused.
 */
fun View.showSoftKeyboard() {
    if (this.requestFocus()) {
        val imm =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
    }
}
