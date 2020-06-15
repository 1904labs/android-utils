package com.labs1904.ui.extensions

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 * Converts pixels into density-independent pixels (DP).
 *
 * @param pixels Pixel value as a float that you would like to convert.
 * @return A float representing the number of density-independent pixels equivalent to your specified pixel value.
 */
fun Context.pxToDp(pixels: Float): Float = pixels / resources.displayMetrics.density

/**
 * Converts density-independent pixels (DP) into pixels.
 *
 * @param dp DP value as a float that you would like to convert.
 * @return A float representing the number of pixels equivalent to your specified DP value.
 */
fun Context.dpToPx(dp: Float): Float = resources.displayMetrics.density * dp

/**
 * Gets the hex representation of the specified color resource.
 *
 * @param colorId Resource ID of the color you wish to get the hex representation of.
 * @return A string representing the hex value of the color.
 */
fun Context.getColorHex(@ColorRes colorId: Int): String =
    ContextCompat.getColor(this, colorId).let {
        String.format("#%06X", (0xFFFFFF and it))
    }