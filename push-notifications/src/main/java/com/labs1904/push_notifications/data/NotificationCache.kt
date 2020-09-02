package com.labs1904.push_notifications.data

import com.labs1904.push_notifications.ScheduledNotification
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

interface NotificationCache {
    fun insertNotifications(notifications: List<ScheduledNotification>)
    fun fetchNotifications(): List<ScheduledNotification>
    fun containsNotificationId(notificationId: Int): Boolean
    fun clearNotifications()
}

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