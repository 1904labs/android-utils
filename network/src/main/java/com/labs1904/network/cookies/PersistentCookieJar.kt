package com.labs1904.network.cookies

import com.labs1904.network.cookies.cache.PersistentCookieCache
import com.labs1904.network.cookies.cache.SessionCookieCache
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * An <a href="https://square.github.io/okhttp/4.x/okhttp/okhttp3/-cookie-jar/">okhttp3.CookieJar</a> that
 * can be externally cleared.
 */
interface MutableCookieJar : CookieJar {
	/**
	 * Remove all cookies from the CookieJar
	 */
	fun clear()

	/**
	 * Remove only session cookies from the CookieJar
	 */
	fun clearSession()
}

/**
 * An <a href="https://tools.ietf.org/html/rfc6265#section-5.3">RFC-6265</a> compliant
 * {@link com.labs1904.network.cookies.MutableCookieJar MutableCookieJar} that properly handles both
 * session and persistent cookies. It delegates the storage of these cookies to the
 * {@link com.labs1904.network.cookies.cache.SessionCookieCache SessionCookieCache} and
 * {@link com.labs1904.network.cookies.cache.PersistentCookieCache PersistentCookieCache} interfaces.
 */
class PersistentCookieJar(
	private val sessionCookieCache: SessionCookieCache,
	private val persistentCookieCache: PersistentCookieCache
) : MutableCookieJar {

	private var loaded = false

	@Synchronized
	override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
		if (!loaded) loadPersistedCookiesIntoMemory()

		cookies.map { SerializableCookie(it) }.forEach {
			sessionCookieCache.insert(it)
			if (it.persistent) persistentCookieCache.insert(it)
		}
	}

	@Synchronized
	override fun loadForRequest(url: HttpUrl): List<Cookie> {
		if (!loaded) loadPersistedCookiesIntoMemory()

		val expired = mutableListOf<SerializableCookie>()
		val valid = mutableListOf<SerializableCookie>()

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
