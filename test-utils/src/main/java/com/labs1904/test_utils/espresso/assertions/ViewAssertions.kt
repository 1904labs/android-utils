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

/**
 * Asserts that the specified view is effectively visible.
 *
 * @param  viewId Id of the view you are targeting.
 */
fun assertViewEffectivelyVisible(@IdRes viewId: Int) {
    onView(withId(viewId)).check(
        matches(
            withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
        )
    )
}

/**
 * Asserts that the specified view is effectively invisible.
 *
 * @param  viewId Id of the view you are targeting.
 */
fun assertViewEffectivelyInvisible(@IdRes viewId: Int) {
    onView(withId(viewId)).check(
        matches(
            withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)
        )
    )
}

/**
 * Asserts that the specified view is effectively gone.
 *
 * @param  viewId Id of the view you are targeting.
 */
fun assertViewEffectivelyGone(@IdRes viewId: Int) {
    onView(withId(viewId)).check(
        matches(
            withEffectiveVisibility(ViewMatchers.Visibility.GONE)
        )
    )
}

/**
 * Asserts that the specified view matches the given Matcher within the specified timeout
 * period (default is 3 seconds). This assertion only waits as long as it needs to.
 *
 * @param  viewId Id of the view you are targeting.
 * @param  viewMatcher The Matcher you are trying to assert on the view.
 * @param  maxWaitMs (optional) Timeout period in milliseconds.
 */
fun assertCustomAssertionByIdWithTimeout(
    @IdRes viewId: Int,
    viewMatcher: Matcher<View?>,
    maxWaitMs: Long = 3000L
) {
    onView(withId(viewId)).perform(waitForMatcher(viewMatcher, maxWaitMs))
}

/**
 * Asserts that the specified view matches the given Matcher within the specified timeout
 * period (default is 3 seconds). This assertion only waits as long as it needs to.
 *
 * @param  viewText Text within the view you are targeting.
 * @param  viewMatcher The Matcher you are trying to assert on the view.
 * @param  maxWaitMs (optional) Timeout period in milliseconds.
 */
fun assertCustomAssertionByTextWithTimeout(
    viewText: String,
    viewMatcher: Matcher<View?>,
    maxWaitMs: Long = 3000L
) {
    onView(withText(viewText)).perform(waitForMatcher(viewMatcher, maxWaitMs))
}

/**
 * Asserts that the specified view matches the given Matcher within the specified timeout
 * period (default is 3 seconds). This assertion only waits as long as it needs to.
 *
 * @param  stringId String resource id of the text within the view you are targeting.
 * @param  viewMatcher The Matcher you are trying to assert on the view.
 * @param  maxWaitMs (optional) Timeout period in milliseconds.
 */
fun assertCustomAssertionByTextWithTimeout(
    @StringRes stringId: Int,
    viewMatcher: Matcher<View?>,
    maxWaitMs: Long = 3000L
) {
    onView(withText(stringId)).perform(waitForMatcher(viewMatcher, maxWaitMs))
}