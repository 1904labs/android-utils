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
import kotlinx.coroutines.*

private const val DEFAULT_BLUR_RADIUS = 25f
private const val BLUR_VIEW_TAG = "BLUR_VIEW_TAG"

/**
 * Generates a Bitmap of this ViewGroup and then blurs it using the given [blurRadius]. It then displays
 * the blurred Bitmap by adding an ImageView to this ViewGroup.
 *
 * @param coroutineScope The LifeCycleCoroutineScope used to launch a coroutine which performs the blurring
 * @param blurColor The color to apply to the blurred bitmap, can be null
 * @param animDuration The duration of the fadeIn() animation used to show the blurred bitmap
 * @param blurRadius The radius of the blur. Supported range 0 < [blurRadius] <= 25
 * @param blurClickListener The OnClickListener that is triggered when the ImageView is clicked, can be null
 */
fun ViewGroup.blur(
	coroutineScope: LifecycleCoroutineScope,
	@ColorRes blurColor: Int? = null,
	animDuration: Long = 0,
	blurRadius: Float = DEFAULT_BLUR_RADIUS,
	blurClickListener: View.OnClickListener? = null
): Job {
	return coroutineScope.launch {
		try {
			val viewGroup = this@blur
			val drawable = viewGroup.bitmap().blur(context, blurRadius).toDrawable(resources)

			blurColor?.let {
				drawable.colorFilter = PorterDuffColorFilter(
					ContextCompat.getColor(context, it),
					PorterDuff.Mode.SRC_OVER
				)
			}

			removeBlur()

			val imageView = AppCompatImageView(context).apply {
				tag = BLUR_VIEW_TAG
				layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
				setBackgroundColor(Color.WHITE)
				setImageDrawable(drawable)
				setOnClickListener(blurClickListener)
			}

			viewGroup.addView(imageView)

			if (animDuration > 0) imageView.fadeIn(animDuration)
		} catch (cancellationException: CancellationException) {
			removeBlur()
		}
	}
}

/**
 * Removes the ImageView that was previously added by ViewGroup.blur()
 *
 * @param animDuration The duration of the fadeOut() animation used to hide the ImageView
 */
fun ViewGroup.removeBlur(animDuration: Long = 0) {
	findViewWithTag<View>(BLUR_VIEW_TAG)?.let {
		if (animDuration > 0) it.fadeOut(animDuration)
		removeView(it)
	}
}

/**
 * Uses RenderScript and the given [blurRadius] to blur this Bitmap
 *
 * @param context The Context used to create the RenderScript instance
 * @param blurRadius The radius of the blur. Supported range 0 < [blurRadius] <= 25
 */
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
