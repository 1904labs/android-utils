package com.labs1904.test_utils.espresso.actions

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId

/**
 * Quickly sets the text of an EditText without popping up the keyboard.
 *
 * @param  viewId Id of the targeted view.
 * @param  text Text you want to set in the EditText.
 */
fun setQuickTextNoKeyboard(@IdRes viewId: Int, text: String) {
    onView(withId(viewId)).perform(replaceText(text))
}
