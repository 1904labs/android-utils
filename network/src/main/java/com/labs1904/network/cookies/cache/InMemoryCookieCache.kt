package com.labs1904.network.cookies.cache

import com.labs1904.network.cookies.IdentifiableCookie
import okhttp3.Cookie

class InMemoryCookieCache(
	private val cookies: MutableSet<IdentifiableCookie> = hashSetOf()
) : CookieCache {

	override fun loadAll(): List<Cookie> =
		cookies.map { it.cookie }

	override fun saveAll(cookies: List<Cookie>) =
		cookies
			.map { IdentifiableCookie(it) }
			.forEach {
				this.cookies.remove(it)
				this.cookies.add(it)
			}

	override fun removeAll(cookies: List<Cookie>) =
		cookies
			.map { IdentifiableCookie(it) }
			.forEach { this.cookies.remove(it) }

	override fun clear() {
		cookies.clear()
	}
}
