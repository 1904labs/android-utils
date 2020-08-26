package com.labs1904.ui.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NavUtils
import com.labs1904.ui.R
import io.reactivex.rxjava3.core.Observable

/**
 * Starts an implicit intent to open up the dialer with the specified Uri.
 *
 * @param uri The Uri you wish to launch the dialer with.
 */
fun Activity.openDialer(uri: Uri) {
    startActivity(Intent(Intent.ACTION_DIAL, uri))
}

/**
 * Sets the transition animations to enter from the left and exit to the right when navigating between
 * activities. This should be called just after your call to finish() or startActivity().
 */
fun Activity.enterLeftExitRight() {
    overridePendingTransition(R.anim.enter_left, R.anim.exit_right)
}

/**
 * Sets the transition animations to enter from the right and exit to the left when navigating between
 * activities. This should be called just after your call to finish() or startActivity().
 */
fun Activity.enterRightExitLeft() {
    overridePendingTransition(R.anim.enter_right, R.anim.exit_left)
}

/**
 * Finishes the current Activity and sets the transition animations to enter from the right and exit
 * to the left.
 */
fun Activity.finishAndExitWithAnimation() {
    finish()
    enterRightExitLeft()
}

/**
 * Finishes the current Activity and sets the transition animations to enter from the left and exit
 * to the right.
 */
fun Activity.finishAndExitWithBackAnimation() {
    finish()
    enterLeftExitRight()
}

/**
 * Navigates up to the previous Activity and maintains a proper back stack via the
 * Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP flags.
 */
fun Activity.navigateUp() {
    NavUtils.getParentActivityIntent(this)?.also {
        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        NavUtils.navigateUpTo(this, it)
    }
}

/**
 * Shows a dialog with an optional specified title and a message. This dialog has a single "Dismiss" button
 * that closes the dialog when pressed.
 *
 * @param title (optional) Title you want to display in the dialog. (Defaults to null)
 * @param message Message you want to display in the dialog.
 */
fun Activity.showDialogWithDismiss(title: String? = null, message: String) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(R.string.dismiss) { dialog, _ -> dialog.dismiss() }
        .show()
}

/**
 * Shows a dialog with an optional specified title and a message. This dialog has a single "Ok" button
 * that closes the dialog when pressed.
 *
 * @param title (optional) Title you want to display in the dialog. (Defaults to null)
 * @param message Message you want to display in the dialog.
 */
fun Activity.showDialogWithOk(title: String? = null, message: String) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
        .show()
}

/**
 * Allows you to easily subscribe to an observable stream indicating whether or not the keyboard is
 * visible or not. This function should be used when the soft input mode on the activity is set to
 * SOFT_INPUT_ADJUST_PAN.
 *
 * @param contentId The resource id of the Activity's root ViewGroup.
 * @return An observable stream of booleans that represent if the keyboard is visible (true) or not (false).
 */
fun Activity.keyboardStatusForAdjustPan(@IdRes contentId: Int): Observable<Boolean> =
    Observable.create<Boolean> { emitter ->
        findViewById<ViewGroup>(contentId).let { rootView ->
            ViewTreeObserver.OnGlobalLayoutListener {
                val screenHeight = rootView.height
                val keypadHeight =
                    screenHeight - Rect().apply { rootView.getWindowVisibleDisplayFrame(this) }.bottom
                emitter.onNext(keypadHeight > screenHeight * 0.15)
            }.let { listener ->
                rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
                emitter.setCancellable {
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
                }
            }
        }
    }.distinctUntilChanged()

/**
 * Allows you to easily subscribe to an observable stream indicating whether or not the keyboard is
 * visible or not. This function should be used when the soft input mode on the activity is set to
 * SOFT_INPUT_ADJUST_RESIZE.
 *
 * @param contentId The resource id of the Activity's root ViewGroup.
 * @return An observable stream of booleans that represent if the keyboard is visible (true) or not (false).
 */
fun Activity.keyboardStatusForAdjustResize(@IdRes contentId: Int): Observable<Boolean> =
    Observable.create<Boolean> { emitter ->
        findViewById<ViewGroup>(contentId).let { root ->
            ViewTreeObserver.OnGlobalLayoutListener {
                val rootViewHeight = root.rootView.height
                val screenHeight = root.height
                val keypadHeight = rootViewHeight - screenHeight
                emitter.onNext(keypadHeight > dpToPx(300.0f))
            }.let { listener ->
                root.viewTreeObserver.addOnGlobalLayoutListener(listener)
                emitter.setCancellable {
                    root.viewTreeObserver.removeOnGlobalLayoutListener(listener)
                }
            }
        }
    }.distinctUntilChanged()

/**
 * Hides the virtual keyboard.
 */
fun Activity.hideKeyboard() {
    currentFocus?.let {
        inputMethodManager().hideSoftInputFromWindow(it.windowToken, 0)
        it.clearFocus()
    }
}

/**
 * Convenience function to get the InputMethodManager in a more concise way.
 *
 * @return The InputMethodManager
 */
fun Activity.inputMethodManager(): InputMethodManager =
    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

/**
 * Slightly more concise way of requesting permissions. This function ensures backwards compatibility.
 *
 * @param permissions An array of the permissions (Strings) you are requesting.
 * @param requestCode An integer that is used within the onRequestPermissionResult(requestCode, permissions, grantResults)
 * callback to allow you to know which request you are currently responding to.
 */
fun Activity.requestPermission(permissions: Array<String>, requestCode: Int) {
    ActivityCompat.requestPermissions(
        this,
        permissions,
        requestCode
    )
}

/**
 * Simple way of getting the view that was set as the content view of the Activity.
 *
 * @return The view that was set as the content view.
 */
fun Activity.getContentView(): View? = (findViewById(android.R.id.content) as? ViewGroup)?.getChildAt(0)