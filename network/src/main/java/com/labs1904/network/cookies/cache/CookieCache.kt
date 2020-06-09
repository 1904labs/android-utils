package com.labs1904.network.cookies.cache

import com.labs1904.network.cookies.SerializableCookie

/**
 * A CookieCache object represents a storage mechanism for cookies.
 */
interface CookieCache {
	/**
	 * Inserts a SerializableCookie into this CookieCache
	 */
	fun insert(cookie: SerializableCookie)

	/**
	 * Inserts a list of SerializableCookies into this CookieCache
	 */
	fun insertAll(cookies: List<SerializableCookie>)

	/**
	 * Removes [cookie] from this CookieCache
	 *
	 * @param cookie The cookie to be removed
	 */
	fun remove(cookie: SerializableCookie)

	/**
	 * Removes all SerializableCookies defined within [cookies] from this CookieCache.
	 */
	fun removeAll(cookies: List<SerializableCookie>)

	/**
	 * Returns all cookies contained within this CookieCache
	 *
	 * @return A list of SerializableCookies contained within the cache
	 */
	fun load(): List<SerializableCookie>

	/**
	 * Clears all cookies from this cache
	 */
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
