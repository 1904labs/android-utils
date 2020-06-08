package com.labs1904.test_utils.espresso.matchers

import android.view.View
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * Matcher that checks that the specified auto link mask matches what is expected.
 *
 * @param  autoLinkMask The value of the  auto link mask you are expecting.
 */
fun withAutoLinkMask(autoLinkMask: Int) = object : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("Expected auto link mask: $autoLinkMask")
    }

    override fun matchesSafely(item: View?): Boolean =
        item != null && item is TextView && item.autoLinkMask == autoLinkMask
}

/**
 * Matcher that checks that the specified font matches what is expected.
 *
 * @param  fontId The resource id of the font you are expecting.
 */
fun withFont(@FontRes fontId: Int) = object : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("Expected font res id: $fontId")
    }

    override fun matchesSafely(item: View?): Boolean =
        item?.context?.let { context ->
            item is TextView && item.typeface == ResourcesCompat.getFont(context, fontId)
        } ?: false
}