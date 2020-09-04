package com.labs1904.ui.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val DEFAULT_BLUR_RADIUS = 25f
private const val BLUR_VIEW_TAG = "BLUR_VIEW_TAG"

fun ViewGroup.blur(
	coroutineScope: LifecycleCoroutineScope,
	@ColorRes blurColor: Int? = null,
	animDuration: Long = 0,
	blurRadius: Float = DEFAULT_BLUR_RADIUS,
	blurClickListener: View.OnClickListener? = null
) {
	coroutineScope.launch {
		val viewGroup = this@blur
		val drawable = viewGroup.bitmap().blur(context, blurRadius).toDrawable(resources)

		blurColor?.let {
			drawable.colorFilter = PorterDuffColorFilter(
				ContextCompat.getColor(context, it),
				PorterDuff.Mode.SRC_OVER
			)
		}

		val imageView = AppCompatImageView(context).apply {
			tag = BLUR_VIEW_TAG
			layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
			setBackgroundColor(Color.WHITE)
			setImageDrawable(drawable)
			setOnClickListener(blurClickListener)
		}

		viewGroup.addView(imageView)

		if (animDuration > 0) imageView.fadeIn(animDuration)
	}
}

fun ViewGroup.removeBlur(animDuration: Long = 0) {
	findViewWithTag<View>(BLUR_VIEW_TAG)?.let {
		if (animDuration > 0) it.fadeOut(animDuration)
		removeView(it)
	}
}

private suspend fun Bitmap.blur(
	context: Context,
	blurRadius: Float
): Bitmap {
	return withContext(Dispatchers.Default) {
		val bitmap = this@blur
		var renderScript: RenderScript? = null
		var input: Allocation? = null
		var output: Allocation? = null
		var blur: ScriptIntrinsicBlur? = null

		try {
			renderScript = RenderScript.create(context)
			input = Allocation.createFromBitmap(
				renderScript,
				bitmap,
				Allocation.MipmapControl.MIPMAP_NONE,
				Allocation.USAGE_SCRIPT
			)
			output = Allocation.createTyped(renderScript, input?.type)
			blur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))

			blur?.setInput(input)
			blur?.setRadius(blurRadius)
			blur?.forEach(output)
			output?.copyTo(bitmap)

			bitmap
		} finally {
			renderScript?.destroy()
			input?.destroy()
			output?.destroy()
			blur?.destroy()
		}
	}
}
