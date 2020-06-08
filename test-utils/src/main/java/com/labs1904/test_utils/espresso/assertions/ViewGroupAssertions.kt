package com.labs1904.test_utils.espresso.assertions

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*

fun assertChildCount(@IdRes parentId: Int, expectedCount: Int) {
    onView(withId(parentId)).check(matches(hasChildCount(expectedCount)))
}

fun assertMinimumChildCount(@IdRes parentId: Int, expectedCount: Int) {
    onView(withId(parentId)).check(matches(hasMinimumChildCount(expectedCount)))
}