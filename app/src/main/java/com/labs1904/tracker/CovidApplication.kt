package com.labs1904.tracker

import androidx.multidex.MultiDexApplication
import com.labs1904.connectivity_notifier.ConnectivityStateHolder.registerConnectivityNetworkCallbacks
import com.labs1904.push_notifications.PushNotificationHelper
import com.labs1904.push_notifications.PushNotificationHelperProvider
import com.labs1904.tracker.utils.PushNotificationHandler
import timber.log.Timber

class CovidApplication : MultiDexApplication(), PushNotificationHelperProvider {

    private var pushNotificationHelper: PushNotificationHandler? = null

    override fun onCreate() {
        super.onCreate()
        registerConnectivityNetworkCallbacks()
        Timber.plant(Timber.DebugTree())
    }

    override fun get(): PushNotificationHelper? {
		if (pushNotificationHelper == null) {
			pushNotificationHelper = PushNotificationHandler(this)
		}
		return pushNotificationHelper
	}
}
