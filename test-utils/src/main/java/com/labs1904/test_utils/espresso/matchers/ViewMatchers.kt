package com.labs1904.test_utils.espresso.matchers

import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.util.HumanReadables
import org.hamcrest.CoreMatchers.any
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.util.concurrent.TimeoutException

/**
 * Matcher that checks that the specified background color matches what is expected.
 *
 * @param  colorId The resource id of the color you are expecting.
 */
fun withBackgroundColor(@ColorRes colorId: Int) = object : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description?) {
        description?.appendText("Expected background color res id: $colorId")
    }

    override fun matchesSafely(item: View?): Boolean =
        (item?.background as? ColorDrawable)?.color == item?.context?.let {
            ContextCompat.getColor(
                it,
                colorId
            )
        }
}

/**
 * Matcher that checks the given matcher is matched on within a specified time out period. This
 * matcher only waits as long as it needs to.
 *
 * @param  viewMatcher The matcher you are trying to assert on the view.
 * @param  maxWaitMs The time out period in milliseconds.
 */
internal fun <T : View?> waitForMatcher(viewMatcher: Matcher<T>, maxWaitMs: Long) =
    object : ViewAction {

        override fun getDescription(): String =
            "wait a maximum of $maxWaitMs ms for the ViewMatcher to match the view"

        override fun getConstraints(): Matcher<View> = any(View::class.java)

        override fun perform(uiController: UiController?, view: View?) {
            uiController?.loopMainThreadUntilIdle()

            val startTime = System.currentTimeMillis()
            val endTime = startTime + maxWaitMs

            do {
                if (viewMatcher.matches(view)) return
                uiController?.loopMainThreadForAtLeast(25)
            } while (System.currentTimeMillis() < endTime)

            throw  PerformException.Builder()
                .withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(TimeoutException())
                .build()
        }
    }