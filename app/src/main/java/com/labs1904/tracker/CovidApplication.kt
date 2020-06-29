package com.labs1904.tracker

import androidx.multidex.MultiDexApplication
import timber.log.Timber

class CovidApplication : MultiDexApplication() {

	override fun onCreate() {
		super.onCreate()

		Timber.plant(Timber.DebugTree())
	}

}
