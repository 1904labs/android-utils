package com.labs1904.test_utils.espresso.matchers

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

fun withBackgroundColor(@ColorRes colorResId: Int) = object : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("Expected background color res id: $colorResId")
    }

    override fun matchesSafely(item: View?): Boolean =
        (item?.background as? ColorDrawable)?.color == item?.context?.let {
            ContextCompat.getColor(
                it,
                colorResId
            )
        }
}