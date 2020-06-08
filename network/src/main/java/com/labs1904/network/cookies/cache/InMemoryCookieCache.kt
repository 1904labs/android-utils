package com.labs1904.network.cookies.cache

import com.labs1904.network.cookies.PersistableCookie

class InMemoryCookieCache(
	private val cookies: MutableSet<PersistableCookie> = hashSetOf()
) : SessionCookieCache {

	override fun insert(cookie: PersistableCookie) {
		cookies.remove(cookie)
		cookies.add(cookie)
	}

	override fun insertAll(cookies: List<PersistableCookie>) {
		this.cookies.removeAll(cookies)
		this.cookies.addAll(cookies)
	}

	override fun remove(cookie: PersistableCookie) {
		this.cookies.remove(cookie)
	}

	override fun removeAll(cookies: List<PersistableCookie>) {
		this.cookies.removeAll(cookies)
	}

	override fun load(): List<PersistableCookie> =
		this.cookies.toList()

	override fun clear() {
		this.cookies.clear()
	}

}
