package com.labs1904.push_notifications.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.labs1904.push_notifications.ScheduledNotification
import com.squareup.moshi.JsonAdapter

/**
 * Entity for the Notification that is stored via Room. This data class also handles
 * serialization and deserialization.
 */
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

    /**
     * Converts the serializable database entity into a ScheduledNotification.
     *
     * @param jsonAdapter A JsonAdapter to deserialize the intent data back into a Map<String, String>.
     * @return The deserialized ScheduledNotification.
     */
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

        /**
         * Converts a ScheduledNotification into a serializable NotificationEntity.
         *
         * @param jsonAdapter A JsonAdapter to serialize the intent data to a String so that Room can store it.
         * @param scheduledNotification The ScheduledNotification you would like to convert.
         * @return The serializable NotificationEntity.
         */
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