package com.labs1904.test_utils.espresso.matchers

import android.view.View
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

fun withAutoLinkMask(autoLinkMask: Int) = object : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("Expected auto link mask: $autoLinkMask")
    }

    override fun matchesSafely(item: View?): Boolean =
        item != null && item is TextView && item.autoLinkMask == autoLinkMask
}

fun withFont(@FontRes fontResId: Int) = object : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("Expected font res id: $fontResId")
    }

    override fun matchesSafely(item: View?): Boolean =
        item?.context?.let { context ->
            item is TextView && item.typeface == ResourcesCompat.getFont(context, fontResId)
        } ?: false
}