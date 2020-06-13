package com.labs1904.ui.extensions

import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.labs1904.ui.R
import com.labs1904.ui.utils.LineDividerDecoration

fun RecyclerView.findFirstVisibleItemPosition(): Int =
    (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

fun RecyclerView.getLastAdapterItemIndex(): Int = adapter?.itemCount?.minus(1) ?: -1

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