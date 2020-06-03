package com.labs1904.test_utils.espresso.matchers

import com.google.android.material.tabs.TabLayout
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

fun withTabSelected(position: Int) = object : TypeSafeMatcher<TabLayout>() {

    override fun describeTo(description: Description?) {
        description?.appendText("Expected tab selected at index $position")
    }

    override fun matchesSafely(item: TabLayout?): Boolean =
        item != null && item.selectedTabPosition == position
}