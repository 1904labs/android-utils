package com.labs1904.test_utils.espresso.assertions

import androidx.annotation.IdRes
import androidx.test.espresso.ViewAssertion
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.junit.Assert.assertEquals

fun assertBadgeVisibility(@IdRes menuItemId: Int, isVisible: Boolean) {
    ViewAssertion { view, _ ->
        assertEquals(isVisible, (view as? BottomNavigationView)?.getBadge(menuItemId)?.isVisible)
    }
}