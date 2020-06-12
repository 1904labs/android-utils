package com.labs1904.ui.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.labs1904.ui.R
import kotlin.math.roundToInt

/**
 * This decorator can be added to a {@link androidx.recyclerview.widget.RecyclerView RecyclerView} to
 * draw a separator between the items. It can take in a custom drawable, work with vertical or horizontal
 * orientations, and can hide the separator on the last item if desired.
 *
 * @param context Activity context used to get the drawable by ID.
 * @param orientation (optional) The orientation of the {@link androidx.recyclerview.widget.RecyclerView RecyclerView}.
 * This is used to know which direction to draw the separators. (Defaults to VERTICAL)
 * @param separatorDrawable (optional) Resource ID for the drawable you wish to use as a separator. (Defaults to a 1dp black line)
 * @param shouldShowLastItem (optional) Flag used to indicate whether or not the separator should show on the last item. (Defaults to false)
 */
class LineDividerDecoration(
    private val context: Context,
    @RecyclerView.Orientation private val orientation: Int = VERTICAL,
    @DrawableRes private val separatorDrawable: Int = R.drawable.black_line_separator,
    private val shouldShowLastItem: Boolean = false
) : RecyclerView.ItemDecoration() {

    private val bounds = Rect()
    private val divider: Drawable? = ContextCompat.getDrawable(context, separatorDrawable)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.layoutManager?.let {
            if (orientation == VERTICAL) {
                drawVertical(c, parent)
            } else {
                drawHorizontal(c, parent)
            }
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        divider?.let { div ->
            canvas.save()

            val left: Int
            val right: Int
            val numItemsToDecorate =
                if (shouldShowLastItem) parent.childCount else parent.childCount - 1

            if (parent.clipToPadding) {
                left = parent.paddingLeft
                right = parent.width - parent.paddingRight
                canvas.clipRect(
                    left,
                    parent.paddingTop,
                    right,
                    parent.height - parent.paddingBottom
                )
            } else {
                left = context.dpToPx(16F.toInt())
                right = parent.width
            }

            for (i in 0 until numItemsToDecorate) {
                val child = parent.getChildAt(i)
                val bottom = bounds.bottom + child.translationY.roundToInt()
                val top = bottom - div.intrinsicHeight

                parent.getDecoratedBoundsWithMargins(child, bounds)
                div.setBounds(left, top, right, bottom)
                div.draw(canvas)
            }

            canvas.restore()
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        divider?.let { div ->
            canvas.save()

            val top: Int
            val bottom: Int
            val numItemsToDecorate =
                if (shouldShowLastItem) parent.childCount else parent.childCount - 1

            if (parent.clipToPadding) {
                top = parent.paddingTop
                bottom = parent.height - parent.paddingBottom
                canvas.clipRect(parent.paddingLeft, top, parent.width - parent.paddingRight, bottom)
            } else {
                top = 0
                bottom = parent.height
            }

            for (i in 0 until numItemsToDecorate) {
                val child = parent.getChildAt(i)
                val right = bounds.right + child.translationX.roundToInt()
                val left = right - div.intrinsicWidth

                parent.getDecoratedBoundsWithMargins(child, bounds)
                div.setBounds(left, top, right, bottom)
                div.draw(canvas)
            }

            canvas.restore()
        }
    }
}