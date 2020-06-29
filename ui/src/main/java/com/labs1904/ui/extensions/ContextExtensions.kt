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

/**
 * A more concise and readable way of checking if you have permission to use the camera.
 *
 * @return A boolean that is true if the CAMERA permission is granted and false otherwise.
 */
fun Context.hasCameraPermission(): Boolean = ContextCompat.checkSelfPermission(
    this,
    android.Manifest.permission.CAMERA
) == androidx.core.content.PermissionChecker.PERMISSION_GRANTED

/**
 * A more concise and readable way of checking if you have permission to record audio.
 *
 * @return A boolean that is true if the RECORD_AUDIO permission is granted and false otherwise.
 */
fun Context.hasAudioPermission(): Boolean = ContextCompat.checkSelfPermission(
    this,
    android.Manifest.permission.RECORD_AUDIO
) == androidx.core.content.PermissionChecker.PERMISSION_GRANTED