package com.labs1904.test_utils.espresso.assertions

import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.labs1904.test_utils.espresso.matchers.waitForMatcher
import org.hamcrest.Matcher


fun assertViewEffectivelyVisible(@IdRes viewId: Int) {
    onView(withId(viewId)).check(
        matches(
            withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        )
    )
}

fun assertViewEffectivelyInvisible(@IdRes viewId: Int) {
    onView(withId(viewId)).check(
        matches(
            withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)
        )
    )
}

fun assertViewEffectivelyGone(@IdRes viewId: Int) {
    onView(withId(viewId)).check(
        matches(
            withEffectiveVisibility(ViewMatchers.Visibility.GONE)
        )
    )
}

fun assertCustomAssertionByIdWithTimeout(
    @IdRes viewId: Int,
    viewMatcher: Matcher<View?>,
    maxWaitMs: Long = 3000L
) {
    onView(withId(viewId)).perform(waitForMatcher(viewMatcher, maxWaitMs))
}

fun assertCustomAssertionByTextWithTimeout(
    viewText: String,
    viewMatcher: Matcher<View?>,
    maxWaitMs: Long = 3000L
) {
    onView(withText(viewText)).perform(waitForMatcher(viewMatcher, maxWaitMs))
}

fun assertCustomAssertionByTextWithTimeout(
    @StringRes stringId: Int,
    viewMatcher: Matcher<View?>,
    maxWaitMs: Long = 3000L
) {
    onView(withText(stringId)).perform(waitForMatcher(viewMatcher, maxWaitMs))
}