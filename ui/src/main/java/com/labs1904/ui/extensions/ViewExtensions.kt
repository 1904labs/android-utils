package com.labs1904.ui.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
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

/**
 * Generates a Bitmap of this View
 */
fun View.bitmap(): Bitmap =
	Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also {
		draw(Canvas(it))
	}

/**
 * Shows this View using an AlphaAnimation (startAlpha = 0, endAlpha = 1)
 *
 * @param animDuration The duration of the AlphaAnimation
 */
fun View.fadeIn(animDuration: Long) {
	visible()
	startAnimation(AlphaAnimation(0f, 1f).apply { duration = animDuration })
}

/**
 * Hides this View using an AlphaAnimation (startAlpha = 1, endAlpha = 0)
 *
 * @param animDuration The duration of the AlphaAnimation
 */
fun View.fadeOut(animDuration: Long) {
	gone()
	startAnimation(AlphaAnimation(1f, 0f).apply { duration = animDuration })
}
