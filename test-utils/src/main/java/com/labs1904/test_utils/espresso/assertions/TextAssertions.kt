package com.labs1904.test_utils.espresso.assertions

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.LayoutAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.hamcrest.core.IsNot.not

fun assertTextIsEllipsized(@IdRes viewId: Int) {
    onView(withId(viewId)).check(matches(not(LayoutAssertions.noEllipsizedText())))
}

fun assertTextIsNotEllipsized(@IdRes viewId: Int) {
    onView(withId(viewId)).check(LayoutAssertions.noEllipsizedText())
}