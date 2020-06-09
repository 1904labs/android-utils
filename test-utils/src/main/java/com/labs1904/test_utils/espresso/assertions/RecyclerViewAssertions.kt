package com.labs1904.test_utils.espresso.assertions

import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import com.labs1904.test_utils.espresso.matchers.withBackgroundColor
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertCustomAssertionAtPosition

/**
 * Asserts the background color of a recycler view at a certain index.
 *
 * @param  recyclerViewId Id of the recycler view you are targeting.
 * @param  position Index you are trying to target.
 * @param  colorId The resource id of the color you are asserting.
 */
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

/**
 * Asserts that the specified view is visible at a particular index.
 *
 * @param  recyclerViewId Id of the recycler view you are targeting.
 * @param  position Index you are trying to target.
 * @param  viewId Id of the view you are asserting on.
 */
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

/**
 * Asserts that the specified view is gone at a particular index.
 *
 * @param  recyclerViewId Id of the recycler view you are targeting.
 * @param  position Index you are trying to target.
 * @param  viewId Id of the view you are asserting on.
 */
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