# Changelog
All notable changes to this module will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0]
### Added
- `NotificationCache` - A cache to hold scheduled notifications.
- `NotificationDatabase` - A database used in the implementation of the cache.
- `NotificationEntity` - Entity for the Notification that is stored via Room. This data class also handles 
serialization and deserialization.
- `NotificationConstants` - Some intent key constants
- `PushNotificationHelper` - Abstraction layer over the cache that ensures the AlarmManager intents are always built the exact same way.
This also gives us a way to work with the cache via an interface defined on the application.
- `ScheduledNotificationReceiver` - A BroadcastReceiver that will send out the notifications at the scheduled time. This will
also listen for device reboots and reschedule any future notifications since AlarmManager intents do not survive device reboots.