package com.labs1904.network.cookies

import com.labs1904.network.cookies.cache.PersistentCookieCache
import com.labs1904.network.cookies.cache.SessionCookieCache
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

interface MutableCookieJar : CookieJar {
	fun clear()
	fun clearSession()
}

class PersistentCookieJar(
	private val sessionCookieCache: SessionCookieCache,
	private val persistentCookieCache: PersistentCookieCache
) : MutableCookieJar {

	private var loaded = false

	@Synchronized
	override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
		if (!loaded) loadPersistedCookiesIntoMemory()

		cookies.map { PersistableCookie(it) }.forEach {
			sessionCookieCache.insert(it)
			if (it.isPersistent()) persistentCookieCache.insert(it)
		}
	}

	@Synchronized
	override fun loadForRequest(url: HttpUrl): List<Cookie> {
		if (!loaded) loadPersistedCookiesIntoMemory()

		val expired = mutableListOf<PersistableCookie>()
		val valid = mutableListOf<PersistableCookie>()

		sessionCookieCache.load().forEach {
			if (it.isExpired()) expired.add(it)
			else if (it.toCookie().matches(url)) valid.add(it)
		}

		sessionCookieCache.removeAll(expired)
		persistentCookieCache.removeAll(expired)

		return valid.map { it.toCookie() }
	}

	@Synchronized
	override fun clear() {
		sessionCookieCache.clear()
		persistentCookieCache.clear()
	}

	@Synchronized
	override fun clearSession() {
		sessionCookieCache.clear()
		loadPersistedCookiesIntoMemory()
	}

	private fun loadPersistedCookiesIntoMemory() {
		sessionCookieCache.insertAll(persistentCookieCache.load())
		loaded = true
	}
}
