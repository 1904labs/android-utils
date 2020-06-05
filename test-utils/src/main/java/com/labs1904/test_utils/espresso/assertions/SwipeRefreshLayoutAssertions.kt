package com.labs1904.test_utils.espresso.assertions

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.labs1904.test_utils.espresso.matchers.isRefreshing
import org.hamcrest.core.IsNot.not

fun assertSwipeRefreshLayoutIsRefreshing(@IdRes viewId: Int) {
    onView(withId(viewId)).check(matches(isRefreshing()))
}

fun assertSwipeRefreshLayoutIsNotRefreshing(@IdRes viewId: Int) {
    onView(withId(viewId)).check(matches(not(isRefreshing())))
}