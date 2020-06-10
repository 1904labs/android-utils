package com.labs1904.ui.bindingadapters

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun View.setVisible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}