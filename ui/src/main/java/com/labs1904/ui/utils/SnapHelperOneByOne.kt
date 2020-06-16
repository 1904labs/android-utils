package com.labs1904.ui.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * This snap helper can be attached to a {@link androidx.recyclerview.widget.RecyclerView RecyclerView} to snap
 * views into the center one by one. This swipe behavior is usually used in a carousel/gallery type implementation where the user can
 * swipe through each item without worrying about their fling velocity.
 */
class SnapHelperOneByOne : LinearSnapHelper() {

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager?,
        velocityX: Int,
        velocityY: Int
    ): Int {

        layoutManager?.takeIf { it is RecyclerView.SmoothScroller.ScrollVectorProvider }?.let {
            (it as LinearLayoutManager).apply {
                return if (velocityX > TARGET_VELOCITY) findLastVisibleItemPosition() else findFirstVisibleItemPosition()
            }
        }

        return RecyclerView.NO_POSITION
    }

    companion object {
        private const val TARGET_VELOCITY: Int = 400
    }
}