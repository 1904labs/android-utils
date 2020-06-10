package com.labs1904.network.cookies.cache

import com.labs1904.network.cookies.SerializableCookie

/**
 * An implementation of {@link com.labs1904.network.cookies.cache.SessionCookieCache SessionCookieCache}
 * that uses a {@link kotlin.collections.MutableSet MutableSet} to store cookies.
 */
class InMemoryCookieCache(
	private val cookies: MutableSet<SerializableCookie> = hashSetOf()
) : SessionCookieCache {

	override fun insert(cookie: SerializableCookie) {
		cookies.remove(cookie)
		cookies.add(cookie)
	}

	override fun insertAll(cookies: List<SerializableCookie>) {
		this.cookies.removeAll(cookies)
		this.cookies.addAll(cookies)
	}

	override fun remove(cookie: SerializableCookie) {
		this.cookies.remove(cookie)
	}

	override fun removeAll(cookies: List<SerializableCookie>) {
		this.cookies.removeAll(cookies)
	}

	override fun load(): List<SerializableCookie> =
		this.cookies.toList()

	override fun clear() {
		this.cookies.clear()
	}

}
