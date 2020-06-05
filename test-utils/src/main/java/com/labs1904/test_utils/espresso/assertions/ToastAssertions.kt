package com.labs1904.test_utils.espresso.assertions

import android.app.Activity
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.core.IsNot.not

fun assertToastTextShowing(@StringRes stringId: Int, activity: Activity) {
    onView(withText(stringId)).inRoot(withDecorView(not(activity.window.decorView)))
        .check(matches(isDisplayed()))
}

fun assertToastTextShowing(expectedText: String, activity: Activity) {
    onView(withText(expectedText)).inRoot(withDecorView(not(activity.window.decorView)))
        .check(matches(isDisplayed()))
}