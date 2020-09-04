package com.labs1904.tracker.utils

import android.app.*
import android.content.Intent
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.labs1904.core.toEpochMillis
import com.labs1904.push_notifications.PushNotificationHelper
import com.labs1904.push_notifications.ScheduledNotification
import com.labs1904.tracker.R
import com.labs1904.tracker.SplashActivity
import com.labs1904.tracker.settings.TimePreference.Companion.TWELVE_HOUR_TIME_FORMAT
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

class PushNotificationHandler(private val app: Application) : PushNotificationHelper(app) {

    override fun createPushNotification(
        title: String,
        body: String,
        intentData: Map<String, String>?
    ): Notification {
        val style = NotificationCompat
            .BigTextStyle()
            .setBigContentTitle(title)
            .bigText(body)

        return NotificationCompat.Builder(
            app,
            app.getString(R.string.default_notification_channel_id)
        )
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification)
            .setColor(ContextCompat.getColor(app, R.color.colorAccent))
            .setStyle(style)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(createNotificationManagerPendingIntent(intentData))
            .setAutoCancel(true)
            .build()
    }

    override fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel(
                app.getString(R.string.default_notification_channel_id),
                app.getString(R.string.default_notification_channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                enableLights(true)
                lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                notificationManager.createNotificationChannel(this)
            }
        }
    }

    @WorkerThread
    fun scheduleWithTimeAndDays(days: List<String>, time: String) {
        val today = LocalDate.now().atStartOfDay()
        var currentDate = today
        val endDate = today.plusMonths(6)
        val pendingNotifications: MutableList<ScheduledNotification> = mutableListOf()

        while (!currentDate.isAfter(endDate)) {
            if (days.map { it.toUpperCase(Locale.ROOT) }
                    .contains(currentDate.dayOfWeek.toString())) {
                LocalTime.parse(time, DateTimeFormatter.ofPattern(TWELVE_HOUR_TIME_FORMAT))
                    .takeIf {
                        currentDate.isAfter(today) || it.isAfter(LocalTime.now())
                    }?.let {
                        val scheduledTimeInMillis = currentDate
                            .atZone(ZoneId.systemDefault())
                            .withHour(it.hour)
                            .withMinute(it.minute)
                            .withSecond(it.second)
                            .toEpochMillis()

                        pendingNotifications.add(
                            ScheduledNotification(
                                NOTIFICATION_ID,
                                scheduledTimeInMillis,
                                TimeZone.getDefault().displayName,
                                app.getString(R.string.push_notification_title),
                                app.getString(R.string.push_notification_body),
                                null
                            )
                        )
                    }
            }
            currentDate = currentDate.plusDays(1)
        }
        schedulePushNotifications(pendingNotifications)
    }

    private fun createNotificationManagerPendingIntent(intentData: Map<String, String>?): PendingIntent? {
        val intent = Intent(app, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intentData?.keys?.forEach { key ->
                putExtra(key, intentData[key])
            }
        }
        return PendingIntent.getActivity(
            app,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    companion object {
        private const val NOTIFICATION_ID = 2332221
    }
}