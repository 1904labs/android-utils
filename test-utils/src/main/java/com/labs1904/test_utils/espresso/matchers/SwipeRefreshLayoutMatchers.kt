package com.labs1904.test_utils.espresso.matchers

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * Matcher that checks that the SwipeRefreshLayout is currently refreshing.
 */
fun isRefreshing(): Matcher<View> =
    object : BoundedMatcher<View, SwipeRefreshLayout>(SwipeRefreshLayout::class.java) {

        override fun describeTo(description: Description?) {
            description?.appendText("is refreshing")
        }

        override fun matchesSafely(item: SwipeRefreshLayout): Boolean = item.isRefreshing
    }
