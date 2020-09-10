package com.labs1904.push_notifications

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.labs1904.push_notifications.data.NOTIFICATION_DB_FILE
import com.labs1904.push_notifications.data.NotificationCache
import com.labs1904.push_notifications.data.NotificationDatabase
import com.labs1904.push_notifications.data.RoomNotificationCache
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * This interface is the mechanism for which our [ScheduledNotificationReceiver] will obtain an instance
 * of our [PushNotificationHelper]. Since BroadcastReceivers are unable to take in arguments within a constructor,
 * we need this interface to be implemented on the Application.
 */
interface PushNotificationHelperProvider {
    fun get(): PushNotificationHelper?
}

/**
 * Abstraction layer over the cache that ensures the AlarmManager intents are always built the exact same way.
 * This in combination with [PushNotificationHelperProvider] gives us a way to interface with the cache within
 * the [ScheduledNotificationReceiver].
 */
abstract class PushNotificationHelper(private val app: Application) {

    /**
     * Convenience get() function to obtain the NotificationManager
     */
    val notificationManager
        get() = app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    /**
     * Convenience get() function to obtain the AlarmManager.
     */
    private val alarmManager
        get() = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /**
     * Access to the NotificationDatabase. This is used to instantiate the cache.
     */
    private val notificationDatabase = Room
        .databaseBuilder(app, NotificationDatabase::class.java, NOTIFICATION_DB_FILE)
        .fallbackToDestructiveMigration()
        .build()

    /**
     * Access to the NotificationCache. This is used to store and retrieve our scheduled push notifications.
     */
    private val notificationCache: NotificationCache = RoomNotificationCache(notificationDatabase)

    /**
     * This function is used to define how the push notifications are created. This is used to ensure
     * that all notifications are created the same exact way so that we are able to cancel them via AlarmManager if necessary.
     *
     * @param title The title of the push notification.
     * @param body The body of the push notification.
     * @param intentData Any intent data you will need when the notification is sent out.
     * @return The Notification that will eventually be sent out.
     */
    abstract fun createPushNotification(
        title: String,
        body: String,
        intentData: Map<String, String>?
    ): Notification

    /**
     * This function is used to define how the notification channel is created. This is used by [sendPushNotification] to ensure
     * that the channel is properly created before sending out the push notification.
     *
     * NOTE: You will need to include proper version checks when implementing this function.
     */
    abstract fun createNotificationChannel()

    /**
     * Send out the specified push notification using the specified Id. This function also calls
     * [createNotificationChannel].
     *
     * @param notificationId Id of the notification going out. This is used to update an existing notification or potentially
     * cancel it.
     * @param notification The notification being sent out.
     */
    fun sendPushNotification(notificationId: Int, notification: Notification) {
        createNotificationChannel()
        notificationManager.notify(notificationId, notification)
    }

    /**
     * Schedules and persists the list of notifications at the appropriate times.
     * This also handles the api version checks.
     *
     * NOTE: This needs to be called within a worker thread.
     *
     * @param notifications List of notifications that need to be scheduled to be sent out.
     */
    @WorkerThread
    fun schedulePushNotifications(notifications: List<ScheduledNotification>) {
        notifications.forEach { scheduledNotification ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    scheduledNotification.scheduledDateTimeMillis,
                    createAlarmManagerPendingIntent(scheduledNotification)
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    scheduledNotification.scheduledDateTimeMillis,
                    createAlarmManagerPendingIntent(scheduledNotification)
                )
            }
        }
        notificationCache.insertNotifications(notifications)
    }

    /**
     * Cancels all scheduled notifications.
     *
     * NOTE: This needs to be called within a worker thread.
     */
    @WorkerThread
    fun cancelScheduledNotifications() {
        notificationCache.fetchNotifications().forEach { notification ->
            alarmManager.cancel(createAlarmManagerPendingIntent(notification))
        }
        notificationCache.clearNotifications()
    }

    /**
     * Checks to see if a notification with that id has already been scheduled.
     *
     * NOTE: This needs to be called within a worker thread.
     *
     * @param id The id you are checking.
     * @return True if the id is already in the cache, false otherwise.
     */
    @WorkerThread
    fun hasAlreadyScheduledNotificationWithId(id: Int): Boolean =
        notificationCache.containsNotificationId(id)

    /**
     * Reschedules all of the notifications that are in the future. This is used by the [ScheduledNotificationReceiver]
     * after a device reboot. Since AlarmManager is cleared when the device is turned off, this is necessary to restore
     * the scheduled notifications.
     *
     * NOTE: This needs to be called within a worker thread.
     */
    @WorkerThread
    internal fun rescheduleFutureNotifications() {
        notificationCache.fetchNotifications().filter {
            it.scheduledDateTimeMillis >= ZonedDateTime.now(it.scheduledTimeZone.toZoneId()).toEpochMillis()
        }.let {
            notificationCache.clearNotifications()
            schedulePushNotifications(it)
        }
    }

    /**
     * Creates a PendingIntent to be used by AlarmManager. This uses the [ScheduledNotification] hashCode as the
     * requestCode since this is reproducible (if we need to cancel), but is also unique to each notification to ensure they
     * all are sent out.
     *
     * @param scheduledNotification The notification that will eventually be sent out.
     * @return The PendingIntent created that contains the notification and its id.
     */
    private fun createAlarmManagerPendingIntent(scheduledNotification: ScheduledNotification): PendingIntent {
        val notificationIntent = Intent(app, ScheduledNotificationReceiver::class.java).apply {
            putExtra(NOTIFICATION_ID_INTENT_KEY, scheduledNotification.id)
            putExtra(
                NOTIFICATION_INTENT_KEY,
                createPushNotification(
                    scheduledNotification.title,
                    scheduledNotification.body,
                    scheduledNotification.intentData
                )
            )
        }

        return PendingIntent.getBroadcast(
            app,
            scheduledNotification.hashCode(),
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    /**
     * Transforms a ZoneDateTime to epoch millis.
     *
     * @return The epoch in milliseconds
     */
    private fun ZonedDateTime.toEpochMillis(): Long = this.toEpochSecond().times(SECONDS_TO_MILLIS_MULTIPLIER)

    /**
     * Transforms a string representation of a timezone into a ZoneId. If this operation fails,
     * the system default is returned.
     *
     * @return The transformed ZoneId if successful, otherwise the system default ZoneId.
     */
    private fun String?.toZoneId(): ZoneId = this?.let {
        try {
            val timeZone = ZoneId.SHORT_IDS[it] ?: it

            ZoneId.of(timeZone)
        } catch (e: Exception) {
            null
        }
    } ?: ZoneId.systemDefault()

    companion object {
        private const val SECONDS_TO_MILLIS_MULTIPLIER = 1000
    }
}

/**
 * A data class to standardize the data necessary to schedule a push notification.
 *
 * @param id - Id used to send the notification. If multiple notifications have the same id, then the notification will be updated
 * and only the latest will be shown to the user.
 * @param scheduledDateTimeMillis The time in millis that this notification should be sent out.
 * @param scheduledTimeZone The initial time zone used to calculate the time in millis. This is used when rescheduling notifications
 * after a device restart. The original TimeZone is required to only schedule future notifications.
 * @param title The title displayed in the push notification.
 * @param body The body message within the push notification.
 * @param intentData The intent data sent out with the push notification.
 */
data class ScheduledNotification(
    val id: Int,
    val scheduledDateTimeMillis: Long,
    val scheduledTimeZone: String,
    val title: String,
    val body: String,
    val intentData: Map<String, String>?
)