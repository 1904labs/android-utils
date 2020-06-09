package com.labs1904.test_utils.espresso.assertions

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*

/**
 * Asserts that the specified view has the specified number of child views.
 *
 * @param  parentId Id of the view you are targeting.
 * @param  expectedCount The expected child count.
 */
fun assertChildCount(@IdRes parentId: Int, expectedCount: Int) {
    onView(withId(parentId)).check(matches(hasChildCount(expectedCount)))
}

/**
 * Asserts that the specified view has the specified minimum amount of child views.
 *
 * @param  parentId Id of the view you are targeting.
 * @param  expectedCount The expected minimum child count.
 */
fun assertMinimumChildCount(@IdRes parentId: Int, expectedCount: Int) {
    onView(withId(parentId)).check(matches(hasMinimumChildCount(expectedCount)))
}