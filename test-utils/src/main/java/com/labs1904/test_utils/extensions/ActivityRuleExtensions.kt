package com.labs1904.test_utils.extensions

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.labs1904.test_utils.TestActivity
import java.util.concurrent.locks.ReentrantLock

/**
 * Extension function to rotate the activity to landscape mode.
 */
fun <T : Activity?> ActivityTestRule<T>.rotateLandscape() {
    this.activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

/**
 * Extension function to rotate the activity to portrait mode.
 */
fun <T : Activity?> ActivityTestRule<T>.rotatePortrait() {
    this.activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

/**
 * Extension function to simplify launching a fragment into a TestActivity so that you may test it
 * in isolation.
 *
 * @param  fragment Fragment you intend to launch.
 * @param  bundle (optional) Bundle arguments you would like to be set on the fragment before launch.
 */
fun <T : TestActivity?> ActivityTestRule<T>.launchFragment(
    fragment: Fragment,
    bundle: Bundle? = null
) {
    val activity =
        this.launchActivity(TestActivity.createIntent(InstrumentationRegistry.getInstrumentation().targetContext))

    fragment.arguments = bundle

    runOnMainThread {
        activity?.supportFragmentManager?.beginTransaction()?.add(android.R.id.content, fragment)
            ?.commitNow()
    }
}

/**
 * Handles running any function you want on the main thread.
 *
 * @param  action Function you would like to run on the main thread.
 */
fun runOnMainThread(action: () -> Unit) {
    val reentrantLock = ReentrantLock()

    val runnable = Runnable {
        reentrantLock.lock()

        try {
            action()
        } finally {
            reentrantLock.unlock()
        }
    }

    if (Looper.myLooper() == Looper.getMainLooper()) {
        runnable.run()
    } else {
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        InstrumentationRegistry.getInstrumentation().runOnMainSync(runnable)
    }
}