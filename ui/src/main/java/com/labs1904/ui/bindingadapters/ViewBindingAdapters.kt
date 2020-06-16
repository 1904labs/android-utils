package com.labs1904.ui.bindingadapters

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * This binding adapter allows us to easily bind to a boolean in xml to toggle the visibility of any view.
 *
 * @param isVisible Toggles the view between VISIBLE and GONE.
 */
@BindingAdapter("isVisible")
fun View.setVisible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}