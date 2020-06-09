package com.labs1904.test_utils.espresso.matchers

import android.view.View
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher that checks that the specified tab is currently selected.
 *
 * @param  position Index of the tab you expect to currently be selected.
 */
fun withTabSelected(position: Int) = object : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("Expected tab selected at index $position")
    }

    override fun matchesSafely(item: View?): Boolean =
        item != null && item is TabLayout && item.selectedTabPosition == position
}