package com.labs1904.test_utils.espresso.assertions

import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.instanceOf

fun assertToolbarTitle(@StringRes stringId: Int) {
    onView(
        allOf(
            isDescendantOfA(instanceOf(Toolbar::class.java)),
            instanceOf(AppCompatTextView::class.java)
        )
    ).check(matches(withText(stringId)))
}

fun assertToolbarTitle(expectedText: String) {
    onView(
        allOf(
            isDescendantOfA(instanceOf(Toolbar::class.java)),
            instanceOf(AppCompatTextView::class.java)
        )
    ).check(matches(withText(expectedText)))
}