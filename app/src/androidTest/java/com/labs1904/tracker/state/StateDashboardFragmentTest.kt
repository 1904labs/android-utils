package com.labs1904.tracker.state

import androidx.test.rule.ActivityTestRule
import com.labs1904.test_utils.TestActivity
import com.labs1904.test_utils.extensions.launchFragment
import org.junit.Rule
import org.junit.Test

class StateDashboardFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    @Test
    fun screen_success() {
        activityRule.launchFragment(StateDashboardFragment(), null)

    }

    @Test
    fun screen_error() {
        activityRule.launchFragment(StateDashboardFragment(), null)

    }

    @Test
    fun screen_loading() {
        activityRule.launchFragment(StateDashboardFragment(), null)

    }

    @Test
    fun pull_to_refresh_stops_on_error() {
        activityRule.launchFragment(StateDashboardFragment(), null)

    }

    @Test
    fun pull_to_refresh_stops_on_success() {
        activityRule.launchFragment(StateDashboardFragment(), null)

    }
}