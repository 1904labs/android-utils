package com.labs1904.test_utils.espresso.assertions

import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import com.labs1904.test_utils.espresso.matchers.withBackgroundColor
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertCustomAssertionAtPosition

fun assertBackgroundColorAtRecyclerPosition(
    @IdRes recyclerViewId: Int,
    position: Int,
    @ColorRes colorId: Int
) {
    assertCustomAssertionAtPosition(
        recyclerViewId,
        position,
        -1,
        matches(withBackgroundColor(colorId))
    )
}

fun assertViewVisibleAtRecyclerPosition(
    @IdRes recyclerViewId: Int,
    position: Int,
    @IdRes viewId: Int
) {
    assertCustomAssertionAtPosition(
        recyclerViewId,
        position,
        viewId,
        matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
    )
}

fun assertViewGoneAtRecyclerPosition(
    @IdRes recyclerViewId: Int,
    position: Int,
    @IdRes viewId: Int
) {
    assertCustomAssertionAtPosition(
        recyclerViewId,
        position,
        viewId,
        matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE))
    )
}