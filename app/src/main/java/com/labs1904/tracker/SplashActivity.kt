package com.labs1904.tracker

import android.animation.Animator
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.labs1904.ui.extensions.finishAndExitWithAnimation
import com.labs1904.ui.utils.AnimationEndListener
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val animationEndListener = object : AnimationEndListener {
            override fun onAnimationEnd(animation: Animator?) {
                MainActivity.launch(this@SplashActivity)
                finishAndExitWithAnimation()
            }
        }

        splash_container
            .animate()
            .setListener(animationEndListener)
            .alpha(1f)
            .setDuration(2000)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }
}