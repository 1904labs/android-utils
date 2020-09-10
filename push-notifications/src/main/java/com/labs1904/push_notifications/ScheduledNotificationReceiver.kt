package com.labs1904.push_notifications

import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A BroadcastReceiver that will send out the notifications at the scheduled time. This will
 * also listen for device reboots and reschedule any future notifications since AlarmManager intents
 * do not survive device reboots.
 *
 * In order for this to work properly, there needs to be a proper implementation of [PushNotificationHelper]
 * as well as an application that implements the [PushNotificationHelperProvider] interface.
 * Since BroadcastReceivers cannot have any constructors with parameters, this interface is used to get an instance of the [PushNotificationHelper].
 */
class ScheduledNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as? PushNotificationHelperProvider)?.get()
            ?.let { pushNotificationHelper ->
                if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
                    CoroutineScope(Dispatchers.IO).launch {
                        pushNotificationHelper.rescheduleFutureNotifications()
                    }
                } else {
                    intent.getParcelableExtra<Notification>(NOTIFICATION_INTENT_KEY)
                        ?.let { notification ->
                            pushNotificationHelper.sendPushNotification(
                                intent.getIntExtra(
                                    NOTIFICATION_ID_INTENT_KEY,
                                    NOTIFICATION_ID_DEFAULT_VALUE
                                ),
                                notification
                            )
                        }
                }
            }
    }

    companion object {
        private const val NOTIFICATION_ID_DEFAULT_VALUE = 100
    }
}