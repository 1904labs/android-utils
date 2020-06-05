package com.labs1904.test_utils.espresso.actions

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.*
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

fun selectTabAtPosition(@IdRes viewId: Int, position: Int) {

    onView(withId(viewId)).perform(object : ViewAction {

        override fun getDescription(): String = "with tab at index $position"

        override fun getConstraints(): Matcher<View> =
            allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

        override fun perform(uiController: UiController?, view: View?) {
            val tabLayout = view as TabLayout
            val tabAtIndex: TabLayout.Tab =
                tabLayout.getTabAt(position) ?: throw PerformException.Builder()
                    .withCause(Throwable("No tab at index $position")).build()

            tabAtIndex.select()
        }
    })
}