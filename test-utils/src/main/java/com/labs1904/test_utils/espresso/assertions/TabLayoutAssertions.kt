package com.labs1904.test_utils.espresso.assertions

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.labs1904.test_utils.espresso.matchers.withTabSelected

fun assertTabSelected(@IdRes viewId: Int, expectedTabIndex: Int) {
    onView(withId(viewId)).check(matches(withTabSelected(expectedTabIndex)))
}