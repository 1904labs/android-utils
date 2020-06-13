package com.labs1904.ui.extensions

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

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