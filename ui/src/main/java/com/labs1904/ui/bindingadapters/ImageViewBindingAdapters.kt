package com.labs1904.ui.bindingadapters

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

/**
 * This binding adapter allows us to easily bind to a string URL to load a network image into an ImageView
 * via Glide. It also handles displaying a placeholder as well as an error drawable.
 *
 * @param url The URL of the image you are intending to load.
 * @param placeholderDrawable The drawable resource id for the image you want to display while the network
 * image is still loading.
 * @param errorDrawable The drawable resource id for the image you want to display when there is an error
 * loading the image.
 */
@BindingAdapter("url", "placeholderDrawable", "errorDrawable")
fun ImageView.showNetworkImage(
    url: String,
    @DrawableRes placeholderDrawable: Int,
    @DrawableRes errorDrawable: Int
) {
    Glide.with(this)
        .load(url)
        .placeholder(placeholderDrawable)
        .error(errorDrawable)
        .into(this)
}