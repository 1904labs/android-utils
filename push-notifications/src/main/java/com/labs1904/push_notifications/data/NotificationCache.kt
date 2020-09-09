package com.labs1904.push_notifications.data

import com.labs1904.push_notifications.ScheduledNotification
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * An interface for storing, retrieving, and clearing notifications.
 */
interface NotificationCache {

    /**
     * Insert notifications into the cache.
     *
     * @param notifications A list of notifications to insert.
     */
    fun insertNotifications(notifications: List<ScheduledNotification>)

    /**
     * Retrieve notifications from the cache.
     *
     * @return A list of notifications from the cache.
     */
    fun fetchNotifications(): List<ScheduledNotification>

    /**
     * Checks if a particular notificationId already exists in the cache or not.
     *
     * @param notificationId The notification id you are wanting to check.
     * @return True if the id already exists within the cache, false otherwise.
     */
    fun containsNotificationId(notificationId: Int): Boolean

    /**
     * Clears notifications from the cache.
     */
    fun clearNotifications()
}

/**
 * An implementation of NotificationCache using a Room database.
 */
internal class RoomNotificationCache(notificationDatabase: NotificationDatabase) : NotificationCache {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val notificationDao = notificationDatabase.notificationDao()
    private val notificationJsonAdapter = moshi.adapter<Map<String, String>>(
        Types.newParameterizedType(Map::class.java, String::class.java, String::class.java))

    override fun insertNotifications(notifications: List<ScheduledNotification>) {
        notifications.map { scheduledNotification ->
            NotificationEntity.fromScheduledNotification(notificationJsonAdapter, scheduledNotification)
        }.let { notificationDao.insertAll(it) }
    }

    override fun fetchNotifications(): List<ScheduledNotification> =
        notificationDao.fetchAll().map { notificationEntity ->
            notificationEntity.toScheduledNotification(notificationJsonAdapter)
        }

    override fun containsNotificationId(notificationId: Int): Boolean = notificationDao.getCountByNotificationId(notificationId) > 0

    override fun clearNotifications() {
        notificationDao.clearAll()
    }
}