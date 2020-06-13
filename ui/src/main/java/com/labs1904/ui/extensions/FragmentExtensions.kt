package com.labs1904.ui.extensions

import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.labs1904.ui.R

fun Fragment.setSoftInputAdjustResize() {
    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
}

fun Fragment.setSoftInputAdjustPan() {
    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
}

fun Fragment.launchExternalUriActivity(
    uri: Uri,
    mimeType: String,
    flags: Int = FLAG_ACTIVITY_NO_HISTORY or FLAG_GRANT_READ_URI_PERMISSION
) {
    val intent = Intent(ACTION_VIEW).apply {
        setDataAndType(uri, mimeType)
        this.flags = flags
    }
    context?.packageManager?.let {
        intent.resolveActivity(it)?.let { startActivity(intent) }
    }
}

fun Fragment.showDialogWithDismiss(title: String? = null, message: String) {
    context?.let {
        AlertDialog.Builder(it)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.dismiss) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}

fun Fragment.showDialogWithOk(title: String? = null, message: String) {
    context?.let {
        AlertDialog.Builder(it)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}

fun Fragment.hideKeyboard() {
    view?.rootView?.let { view ->
        inputMethodManager()
            .hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        view.clearFocus()
    }
}

fun Fragment.showKeyboard(view: View) {
    if (view.requestFocus()) {
        inputMethodManager().showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Fragment.inputMethodManager(): InputMethodManager =
    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager