package com.labs1904.push_notifications.data

import androidx.room.*

/**
 * Notification database file name
 */
const val NOTIFICATION_DB_FILE = "notification.db"

/**
 * Notification table name
 */
const val NOTIFICATION_TABLE = "notifications"

/**
 * A NotificationDatabase implemented using Room. This is used to store and retrieve all of the
 * locally scheduled push notifications.
 */
@Database(
    entities = [NotificationEntity::class],
    version = 1
)
internal abstract class NotificationDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}

/**
 * Doa for the NotificationDatabase. This tells Room how to handle each function call.
 */
@Dao
internal interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(notifications: List<NotificationEntity>)

    @Query("SELECT * FROM $NOTIFICATION_TABLE")
    fun fetchAll(): List<NotificationEntity>

    @Query("SELECT count(*) FROM $NOTIFICATION_TABLE WHERE notificationId=:notificationId")
    fun getCountByNotificationId(notificationId: Int): Int

    @Query("DELETE FROM $NOTIFICATION_TABLE")
    fun clearAll()
}