package com.labs1904.network.cookies.cache

import com.labs1904.network.cookies.PersistableCookie

interface CookieCache {
	fun insert(cookie: PersistableCookie)
	fun insertAll(cookies: List<PersistableCookie>)
	fun remove(cookie: PersistableCookie)
	fun removeAll(cookies: List<PersistableCookie>)
	fun load(): List<PersistableCookie>
	fun clear()
}

interface SessionCookieCache : CookieCache

interface PersistentCookieCache : CookieCache
