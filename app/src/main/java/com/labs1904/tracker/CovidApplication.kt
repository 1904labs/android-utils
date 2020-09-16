package com.labs1904.tracker

import androidx.multidex.MultiDexApplication
import com.labs1904.connectivity_notifier.ConnectivityStateHolder.registerConnectivityNetworkCallbacks
import com.labs1904.push_notifications.BasePushNotificationHelper
import com.labs1904.push_notifications.PushNotificationHelperProvider
import com.labs1904.tracker.utils.PushNotificationHelper
import timber.log.Timber

class CovidApplication : MultiDexApplication(), PushNotificationHelperProvider {

    private var pushNotificationHelper: PushNotificationHelper? = null

    override fun onCreate() {
        super.onCreate()
        registerConnectivityNetworkCallbacks()
        Timber.plant(Timber.DebugTree())
    }

    override fun get(): BasePushNotificationHelper? {
		if (pushNotificationHelper == null) {
			pushNotificationHelper = PushNotificationHelper(this)
		}
		return pushNotificationHelper
	}
}
