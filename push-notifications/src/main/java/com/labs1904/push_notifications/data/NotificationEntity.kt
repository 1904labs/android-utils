package com.labs1904.push_notifications.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.labs1904.push_notifications.ScheduledNotification
import com.squareup.moshi.JsonAdapter

@Entity(tableName = NOTIFICATION_TABLE)
internal data class NotificationEntity(
    val notificationId: Int,
    val scheduledDateTimeMillis: Long,
    val scheduledTimeZone: String,
    val title: String,
    val body: String,
    val intentData: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
) {

    fun toScheduledNotification(jsonAdapter: JsonAdapter<Map<String, String>>): ScheduledNotification =
        ScheduledNotification(
            notificationId,
            scheduledDateTimeMillis,
            scheduledTimeZone,
            title,
            body,
            jsonAdapter.fromJson(intentData)
        )

    companion object {
        fun fromScheduledNotification(
            jsonAdapter: JsonAdapter<Map<String, String>>,
            scheduledNotification: ScheduledNotification
        ): NotificationEntity = NotificationEntity(
            scheduledNotification.id,
            scheduledNotification.scheduledDateTimeMillis,
            scheduledNotification.scheduledTimeZone,
            scheduledNotification.title,
            scheduledNotification.body,
            jsonAdapter.toJson(scheduledNotification.intentData)
        )
    }
}