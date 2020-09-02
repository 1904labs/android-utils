package com.labs1904.push_notifications.data

import androidx.room.*

const val NOTIFICATION_DB_FILE = "notification.db"
const val NOTIFICATION_TABLE = "notifications"

@Database(
    entities = [NotificationEntity::class],
    version = 1
)
internal abstract class NotificationDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao
}

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