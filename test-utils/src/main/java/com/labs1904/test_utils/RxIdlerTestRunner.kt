package com.labs1904.test_utils

import androidx.test.runner.AndroidJUnitRunner
import com.squareup.rx3.idler.Rx3Idler
import io.reactivex.rxjava3.plugins.RxJavaPlugins

class RxIdlerTestRunner : AndroidJUnitRunner() {

    override fun onStart() {
        RxJavaPlugins.setInitComputationSchedulerHandler(Rx3Idler.create("RxJava 3.x Computation Scheduler"))
        RxJavaPlugins.setInitIoSchedulerHandler(Rx3Idler.create("RxJava 3.x IO Scheduler"))
        super.onStart()
    }
}