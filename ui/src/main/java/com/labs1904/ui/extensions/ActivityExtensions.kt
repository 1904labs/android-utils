package com.labs1904.ui.extensions

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.IdRes
import androidx.core.app.NavUtils
import com.labs1904.ui.R
import io.reactivex.rxjava3.core.Observable

fun Activity.openDialer(uri: Uri) {
    this.startActivity(Intent(Intent.ACTION_DIAL, uri))
}

fun Activity.enterLeftExitRight() {
    this.overridePendingTransition(R.anim.enter_left, R.anim.exit_right)
}

fun Activity.enterRightExitLeft() {
    this.overridePendingTransition(R.anim.enter_right, R.anim.exit_left)
}

fun Activity.finishAndExitWithAnimation() {
    this.finish()
    enterRightExitLeft()
}

fun Activity.finishAndExitWithBackAnimation() {
    this.finish()
    enterLeftExitRight()
}

fun Activity.navigateUp() {
    NavUtils.getParentActivityIntent(this)?.also {
        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        NavUtils.navigateUpTo(this, it)
    }
}

fun Activity.keyboardStatusForAdjustPan(@IdRes contentId: Int): Observable<Boolean> =
    Observable.create<Boolean> { emitter ->
        findViewById<ViewGroup>(contentId).let { rootView ->
            ViewTreeObserver.OnGlobalLayoutListener {
                val screenHeight = rootView.height
                val keypadHeight = screenHeight - Rect().apply{ rootView.getWindowVisibleDisplayFrame(this) }.bottom
                emitter.onNext(keypadHeight > screenHeight * 0.15)
            }.let { listener ->
                rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
                emitter.setCancellable {
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
                }
            }
        }
    }.distinctUntilChanged()

fun Activity.keyboardStatusForAdjustResize(@IdRes contentId: Int): Observable<Boolean> =
    Observable.create<Boolean> { emitter ->
        findViewById<ViewGroup>(contentId).let { root ->
            ViewTreeObserver.OnGlobalLayoutListener {
                val rootViewHeight = root.rootView.height
                val screenHeight = root.height
                val keypadHeight = rootViewHeight - screenHeight
                emitter.onNext(keypadHeight > dpToPx(300.0f))
            }.let { listener ->
                root.viewTreeObserver.addOnGlobalLayoutListener(listener)
                emitter.setCancellable {
                    root.viewTreeObserver.removeOnGlobalLayoutListener(listener)
                }
            }
        }
    }.distinctUntilChanged()