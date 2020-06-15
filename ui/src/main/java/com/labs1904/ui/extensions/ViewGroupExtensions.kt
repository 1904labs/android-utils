package com.labs1904.ui.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * Used to inflate a view by its ID into a ViewGroup.
 *
 * @param layoutId Id of the layout you wish to inflate.
 * @return View that was inflated.
 */
fun ViewGroup.inflate(@LayoutRes layoutId: Int): View =
    LayoutInflater.from(context).inflate(layoutId, this, false)