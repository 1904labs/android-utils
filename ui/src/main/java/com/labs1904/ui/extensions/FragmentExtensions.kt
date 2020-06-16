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

/**
 * Sets the soft input mode to SOFT_INPUT_ADJUST_RESIZE. This causes the view to resize itself to the
 * area not taken up by the virtual keyboard.
 */
fun Fragment.setSoftInputAdjustResize() {
    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
}

/**
 * Sets the soft input mode to SOFT_INPUT_ADJUST_PAN. This causes the view to remain the same size
 * when the virtual keyboard pops up and adjusts the pan when necessary.
 */
fun Fragment.setSoftInputAdjustPan() {
    activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
}

/**
 * This starts an external activity with a specified URI and mime type. This can be used to open up
 * web pages in the browser, PDFs in a PDF viewer, and virtually any other file type you wish as long
 * as there is an app on the phone that is capable of handling the file type requested.
 *
 * @param uri The web url or path to a file that you would like to open.
 * @param mimeType The type of file you are trying to open.
 * @param flags (optional) Additional flags you would like to send in the intent. (Defaults to FLAG_ACTIVITY_NO_HISTORY or FLAG_GRANT_READ_URI_PERMISSION)
 */
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

/**
 * Shows a dialog with an optional specified title and a message. This dialog has a single "Dismiss" button
 * that closes the dialog when pressed.
 *
 * @param title (optional) Title you want to display in the dialog. (Defaults to null)
 * @param message Message you want to display in the dialog.
 */
fun Fragment.showDialogWithDismiss(title: String? = null, message: String) {
    context?.let {
        AlertDialog.Builder(it)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.dismiss) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}

/**
 * Shows a dialog with an optional specified title and a message. This dialog has a single "Ok" button
 * that closes the dialog when pressed.
 *
 * @param title (optional) Title you want to display in the dialog. (Defaults to null)
 * @param message Message you want to display in the dialog.
 */
fun Fragment.showDialogWithOk(title: String? = null, message: String) {
    context?.let {
        AlertDialog.Builder(it)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }
}

/**
 * Hides the virtual keyboard.
 */
fun Fragment.hideKeyboard() {
    view?.rootView?.let { view ->
        inputMethodManager()
            .hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        view.clearFocus()
    }
}

/**
 * Shows the virtual keyboard.
 */
fun Fragment.showKeyboard(view: View) {
    if (view.requestFocus()) {
        inputMethodManager().showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

/**
 * Convenience function to get the InputMethodManager in a more concise way.
 *
 * @return The InputMethodManager
 */
fun Fragment.inputMethodManager(): InputMethodManager =
    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager