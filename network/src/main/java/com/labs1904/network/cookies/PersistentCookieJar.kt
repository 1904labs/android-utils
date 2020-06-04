package com.labs1904.network.cookies

import com.labs1904.network.cookies.cache.CookieCache
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

interface MutableCookieJar : CookieJar {
	fun clear()
	fun clearSession()
}

class PersistentCookieJar(
	private val sessionCookieCache: CookieCache,
	private val persistentCookieCache: CookieCache
) : MutableCookieJar {

	private var loaded = false

	@Synchronized
	override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
		if (!loaded) fillCacheWithPersistedCookies()

		sessionCookieCache.saveAll(cookies)
		persistentCookieCache.saveAll(
			cookies.filter { it.persistent }
		)
	}

	@Synchronized
	override fun loadForRequest(url: HttpUrl): List<Cookie> {
		if (!loaded) fillCacheWithPersistedCookies()

		val expiredCookies = mutableListOf<Cookie>()
		val validCookies = mutableListOf<Cookie>()

		sessionCookieCache.loadAll().forEach { cookie ->
			if (cookie.isExpired()) expiredCookies.add(cookie)
			else if (cookie.matches(url)) validCookies.add(cookie)
		}

		sessionCookieCache.removeAll(expiredCookies)
		persistentCookieCache.removeAll(expiredCookies)

		return validCookies
	}

	@Synchronized
	override fun clear() {
		sessionCookieCache.clear()
		persistentCookieCache.clear()
	}

	@Synchronized
	override fun clearSession() {
		sessionCookieCache.clear()
		fillCacheWithPersistedCookies()
	}

	private fun fillCacheWithPersistedCookies() {
		sessionCookieCache.saveAll(persistentCookieCache.loadAll())
		loaded = true
	}
}

fun Cookie.isExpired(): Boolean =
	expiresAt < System.currentTimeMillis()
