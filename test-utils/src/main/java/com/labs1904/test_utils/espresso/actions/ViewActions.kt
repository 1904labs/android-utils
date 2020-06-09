package com.labs1904.test_utils.espresso.actions

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

/**
 * Clicks a view that may not fully be visible without throwing an exception.
 *
 * @param  matcher Matcher used to target the desired view to be clicked.
 */
fun clickOffScreenView(matcher: Matcher<View>) {

    onView(matcher).perform(object : ViewAction {
        override fun getDescription(): String = "Click off screen view"

        override fun getConstraints(): Matcher<View> = allOf(isEnabled(), isClickable())

        override fun perform(uiController: UiController?, view: View?) {
            view?.performClick()
        }
    })
}