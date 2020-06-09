package com.labs1904.test_utils.espresso.assertions

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.LayoutAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.core.IsNot.not

/**
 * Asserts that the text is ellipsized within the specified view.
 *
 * @param  viewId Id of the view you are targeting.
 */
fun assertTextIsEllipsized(@IdRes viewId: Int) {
    onView(withId(viewId)).check(matches(not(LayoutAssertions.noEllipsizedText())))
}

/**
 * Asserts that the text is not ellipsized within the specified view.
 *
 * @param  viewId Id of the view you are targeting.
 */
fun assertTextIsNotEllipsized(@IdRes viewId: Int) {
    onView(withId(viewId)).check(LayoutAssertions.noEllipsizedText())
}