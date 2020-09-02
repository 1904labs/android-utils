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

interface PushNotificationHelperProvider {
    fun get(): PushNotificationHelper
}

abstract class PushNotificationHelper(private val app: Application) {

    private val notificationManager
        get() = app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val alarmManager
        get() = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private val notificationDatabase = Room
        .databaseBuilder(app, NotificationDatabase::class.java, NOTIFICATION_DB_FILE)
        .fallbackToDestructiveMigration()
        .build()

    private val notificationCache: NotificationCache = RoomNotificationCache(notificationDatabase)

    abstract fun createPushNotification(
        title: String,
        body: String,
        intentData: Map<String, String>?
    ): Notification

    abstract fun createNotificationChannel()

    fun sendPushNotification(notificationId: Int, notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        notificationManager.notify(notificationId, notification)
    }

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

    @WorkerThread
    fun cancelScheduledNotifications() {
        notificationCache.fetchNotifications().forEach { notification ->
            alarmManager.cancel(createAlarmManagerPendingIntent(notification))
        }
        notificationCache.clearNotifications()
    }

    @WorkerThread
    fun hasAlreadyScheduledNotificationWithId(id: Int): Boolean =
        notificationCache.containsNotificationId(id)

    @WorkerThread
    internal fun rescheduleFutureNotifications() {
        notificationCache.fetchNotifications().filter {
            it.scheduledDateTimeMillis >= ZonedDateTime.now(it.scheduledTimeZone.toZoneId()).toEpochMillis()
        }.let {
            notificationCache.clearNotifications()
            schedulePushNotifications(it)
        }
    }

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

    private fun ZonedDateTime.toEpochMillis(): Long = this.toEpochSecond().times(SECONDS_TO_MILLIS_MULTIPLIER)

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

data class ScheduledNotification(
    val id: Int,
    val scheduledDateTimeMillis: Long,
    val scheduledTimeZone: String,
    val title: String,
    val body: String,
    val intentData: Map<String, String>?
)