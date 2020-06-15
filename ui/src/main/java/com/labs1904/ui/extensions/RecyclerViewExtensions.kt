package com.labs1904.ui.extensions

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.labs1904.ui.R
import com.labs1904.ui.utils.LineDividerDecoration

/**
 * Finds the position of the first visible item in the {@link androidx.recyclerview.widget.RecyclerView RecyclerView}.
 *
 * @return Position of the first visible item.
 */
fun RecyclerView.findFirstVisibleItemPosition(): Int =
    (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

/**
 * Finds the index of the last item in the adapter.
 *
 * @return Index of the last item in the adapter.
 */
fun RecyclerView.getLastAdapterItemIndex(): Int = adapter?.itemCount?.minus(1) ?: -1

/**
 * Adds an item separator between the {@link androidx.recyclerview.widget.RecyclerView RecyclerView}
 * items. It can take in a custom drawable, work with vertical or horizontal orientations, and can
 * hide the separator on the last item if desired.
 *
 * @param orientation (optional) The orientation of the {@link androidx.recyclerview.widget.RecyclerView RecyclerView}.
 * This is used to know which direction to draw the separators. (Defaults to VERTICAL)
 * @param separatorDrawable (optional) Resource ID for the drawable you wish to use as a separator. (Defaults to a 1dp black line)
 * @param shouldShowLastItem (optional) Flag used to indicate whether or not the separator should show on the last item. (Defaults to false)
 */
fun RecyclerView.addItemSeparator(
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    @DrawableRes separatorDrawable: Int = R.drawable.black_line_separator,
    shouldShowLastItem: Boolean = false
) {
    context?.let { ctx ->
        val itemDecoration =
            LineDividerDecoration(ctx, orientation, separatorDrawable, shouldShowLastItem)
        addItemDecoration(itemDecoration)
    }
}