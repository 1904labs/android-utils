package com.labs1904.test_utils.espresso.assertions

import androidx.annotation.IdRes
import androidx.test.espresso.ViewAssertion
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.junit.Assert.assertEquals

/**
 * Asserts a badge's visibility on a menu item.
 *
 * @param  menuItemId Id of the menu item you are targeting.
 * @param  isVisible Boolean indicating the expected visibility of the badge.
 */
fun assertBadgeVisibility(@IdRes menuItemId: Int, isVisible: Boolean) {
    ViewAssertion { view, _ ->
        assertEquals(isVisible, (view as? BottomNavigationView)?.getBadge(menuItemId)?.isVisible)
    }
}