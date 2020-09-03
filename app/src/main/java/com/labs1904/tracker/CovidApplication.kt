package com.labs1904.tracker

import androidx.multidex.MultiDexApplication
import com.labs1904.connectivity_notifier.ConnectivityStateHolder.registerConnectivityNetworkCallbacks
import timber.log.Timber

class CovidApplication : MultiDexApplication() {

	override fun onCreate() {
		super.onCreate()
		registerConnectivityNetworkCallbacks()
		Timber.plant(Timber.DebugTree())
	}

}
