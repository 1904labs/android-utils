package com.labs1904.test_utils.espresso.assertions

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.labs1904.test_utils.espresso.matchers.withTabSelected

/**
 * Asserts that the specified tab index is currently selected within the TabLayout.
 *
 * @param  viewId Id of the TabLayout you are targeting.
 * @param  expectedTabIndex Index you are expecting to currently be selected.
 */
fun assertTabSelected(@IdRes viewId: Int, expectedTabIndex: Int) {
    onView(withId(viewId)).check(matches(withTabSelected(expectedTabIndex)))
}