package com.labs1904.test_utils.espresso.assertions

import android.app.Activity
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.core.IsNot.not

/**
 * Asserts that the specified text is currently showing within a Toast.
 *
 * @param  stringId String resource id of the text you expect to be showing.
 * @param  activity The current activity the toast is expected to be showing in.
 */
fun assertToastTextShowing(@StringRes stringId: Int, activity: Activity) {
    onView(withText(stringId)).inRoot(withDecorView(not(activity.window.decorView)))
        .check(matches(isDisplayed()))
}

/**
 * Asserts that the specified text is currently showing within a Toast.
 *
 * @param  expectedText The text you expect to be showing.
 * @param  activity The current activity the toast is expected to be showing in.
 */
fun assertToastTextShowing(expectedText: String, activity: Activity) {
    onView(withText(expectedText)).inRoot(withDecorView(not(activity.window.decorView)))
        .check(matches(isDisplayed()))
}