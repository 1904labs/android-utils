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
	private val cookieCache: CookieCache,
	private val cookiePersistor: CookiePersistor
) : MutableCookieJar {

	private var loaded = false

	@Synchronized
	override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
		if (!loaded) fillCacheWithPersistedCookies()

		cookieCache.saveAll(cookies)
		cookiePersistor.saveAll(
			cookies.filter { it.persistent }
		)
	}

	@Synchronized
	override fun loadForRequest(url: HttpUrl): List<Cookie> {
		if (!loaded) fillCacheWithPersistedCookies()

		val expiredCookies = mutableListOf<Cookie>()
		val validCookies = mutableListOf<Cookie>()

		cookieCache.loadAll().forEach { cookie ->
			if (cookie.isExpired()) expiredCookies.add(cookie)
			else if (cookie.matches(url)) validCookies.add(cookie)
		}

		cookieCache.removeAll(expiredCookies)
		cookiePersistor.removeAll(expiredCookies)

		return validCookies
	}

	@Synchronized
	override fun clear() {
		cookieCache.clear()
		cookiePersistor.clear()
	}

	@Synchronized
	override fun clearSession() {
		cookieCache.clear()
		fillCacheWithPersistedCookies()
	}

	private fun fillCacheWithPersistedCookies() {
		cookieCache.saveAll(cookiePersistor.loadAll())
		loaded = true
	}
}

fun Cookie.isExpired(): Boolean =
	expiresAt < System.currentTimeMillis()
