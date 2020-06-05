package com.labs1904.test_utils.espresso.actions

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId

fun setQuickTextNoKeyboard(@IdRes viewId: Int, text: String) {
    onView(withId(viewId)).perform(replaceText(text))
}
