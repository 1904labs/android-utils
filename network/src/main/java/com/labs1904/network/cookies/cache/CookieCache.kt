package com.labs1904.network.cookies.cache

import com.labs1904.network.cookies.SerializableCookie

/**
 * A CookieCache object represents a storage mechanism for cookies.
 */
interface CookieCache {
	fun insert(cookie: SerializableCookie)
	fun insertAll(cookies: List<SerializableCookie>)
	fun remove(cookie: SerializableCookie)
	fun removeAll(cookies: List<SerializableCookie>)
	fun load(): List<SerializableCookie>
	fun clear()
}

/**
 * A SessionCookieCache object represents a storage mechanism for session cookies. Session cookies should be stored in memory
 * and never written to disk. They are valid only for the current session, and will be lost when the application process is killed.
 */
interface SessionCookieCache : CookieCache

/**
 * A PersistentCookieCache object represents a storage mechanism for persistent cookies. Persistent cookies have an expiration date,
 * and should be stored to disk, only being removed when they have expired.
 */
interface PersistentCookieCache : CookieCache
