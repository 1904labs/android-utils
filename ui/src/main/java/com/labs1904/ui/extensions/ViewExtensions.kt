package com.labs1904.ui.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout

/**
 * Sets the view's visibility to VISIBLE.
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * Sets the view's visibility to INVISIBLE.
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Sets the view's visibility to GONE.
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * Sets the view to enabled.
 */
fun View.enable() {
    isEnabled = true
}

/**
 * Sets the view to disabled.
 */
fun View.disable() {
    isEnabled = false
}

/**
 * Climbs the view hierarchy to find the outermost ViewGroup still contained within the app. This is
 * useful when trying to display a custom Snackbar from any view.
 *
 * @return The outermost ViewGroup within the application.
 */
fun View?.findOutermostViewGroup(): ViewGroup? {
    var currentView = this
    var fallback: ViewGroup? = null

    do {
        if (currentView is CoordinatorLayout || currentView is FrameLayout) {
            val viewGroup = currentView as ViewGroup
            if (android.R.id.content == currentView.id) return viewGroup
            fallback = viewGroup
        }

        currentView = currentView?.parent as? View

    } while (currentView != null)

    return fallback
}