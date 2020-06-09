package com.labs1904.network.cookies.cache

import com.labs1904.network.cookies.SerializableCookie

interface CookieCache {
	fun insert(cookie: SerializableCookie)
	fun insertAll(cookies: List<SerializableCookie>)
	fun remove(cookie: SerializableCookie)
	fun removeAll(cookies: List<SerializableCookie>)
	fun load(): List<SerializableCookie>
	fun clear()
}

interface SessionCookieCache : CookieCache

interface PersistentCookieCache : CookieCache
