package com.labs1904.network.cookies

import okhttp3.Cookie

class InMemoryCookieCache(
	private val cookies: MutableSet<PersistableCookie> = hashSetOf()
) : CookieCache {

	override fun loadAll(): List<Cookie> =
		cookies.map { it.toCookie() }

	override fun saveAll(cookies: List<Cookie>) =
		cookies
			.map { PersistableCookie(it) }
			.forEach {
				this.cookies.remove(it)
				this.cookies.add(it)
			}

	override fun removeAll(cookies: List<Cookie>) =
		cookies
			.map { PersistableCookie(it) }
			.forEach { this.cookies.remove(it) }

	override fun clear() {
		cookies.clear()
	}
}
