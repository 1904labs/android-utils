package com.labs1904.ui.utils

import android.animation.Animator

/**
 * This interface cuts down on the boilerplate code needed when you are trying to implement an
 * {@link android.animation.Animator.AnimatorListener AnimatorListener} when you only care about the
 * implementation for when the animation ends.
 */
interface AnimationEndListener : Animator.AnimatorListener {

    override fun onAnimationRepeat(animation: Animator?) = Unit

    override fun onAnimationCancel(animation: Animator?)= Unit

    override fun onAnimationStart(animation: Animator?) = Unit
}