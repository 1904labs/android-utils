package com.labs1904.ui.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.pxToDp(pixels: Float): Float = pixels / resources.displayMetrics.density

fun Context.dpToPx(dp: Float): Float = resources.displayMetrics.density * dp

fun Context.getColorHex(@ColorRes colorId: Int): String =
    ContextCompat.getColor(this, colorId).let {
        String.format("#%06X", (0xFFFFFF and it))
    }