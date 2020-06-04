package com.labs1904.network.cookies.cache

import com.labs1904.network.cookies.IdentifiableCookie
import okhttp3.Cookie

class InMemoryCookieCache(
	private val cookies: MutableSet<IdentifiableCookie> = hashSetOf()
) : CookieCache {

	override fun addAll(newCookies: List<Cookie>) =
		newCookies
			.map { IdentifiableCookie(it) }
			.forEach {
				cookies.remove(it)
				cookies.add(it)
			}

	override fun removeAll(cookiesToRemove: List<Cookie>) =
		cookiesToRemove
			.map { IdentifiableCookie(it) }
			.forEach { cookies.remove(it) }

	override fun clear() {
		cookies.clear()
	}

	override fun iterator(): Iterator<Cookie> = InMemoryCookieCacheIterator()

	inner class InMemoryCookieCacheIterator : Iterator<Cookie> {
		private val iterator = cookies.iterator()

		override fun hasNext(): Boolean = iterator.hasNext()

		override fun next(): Cookie = iterator.next().cookie
	}
}
