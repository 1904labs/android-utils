package com.labs1904.test_utils

import androidx.test.runner.AndroidJUnitRunner
import com.squareup.rx3.idler.Rx3Idler
import io.reactivex.rxjava3.plugins.RxJavaPlugins

/**
 * This is a pre-configured test runner that uses RxIdler to wrap all RxJava calls with IdlingResources.
 * This helps Espresso know when these asynchronous calls are complete and Espresso is safe to perform
 * its view assertions. This helps cut down on some potential test flakiness due to asynchronous
 * operations.
 */
class RxIdlerTestRunner : AndroidJUnitRunner() {

    override fun onStart() {
        RxJavaPlugins.setInitComputationSchedulerHandler(Rx3Idler.create("RxJava 3.x Computation Scheduler"))
        RxJavaPlugins.setInitIoSchedulerHandler(Rx3Idler.create("RxJava 3.x IO Scheduler"))
        super.onStart()
    }
}