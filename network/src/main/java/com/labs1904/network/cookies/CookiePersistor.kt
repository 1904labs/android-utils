package com.labs1904.network.cookies

import okhttp3.Cookie

interface CookiePersistor {
	fun loadAll(): List<Cookie>
	fun saveAll(cookies: List<Cookie>)
	fun removeAll(cookies: List<Cookie>)
	fun clear()
}

class RoomCookiePersistor(
	private val cookieDataSource: CookieDataSource
) : CookiePersistor {

	override fun loadAll(): List<Cookie> =
		cookieDataSource.load().map { it.toCookie() }

	override fun saveAll(cookies: List<Cookie>) =
		cookies.map { PersistableCookie(it) }.toList().let { cookieDataSource.insert(it) }

	override fun removeAll(cookies: List<Cookie>) =
		cookies.map { PersistableCookie(it) }.toList().let { cookieDataSource.remove(it) }

	override fun clear() =
		cookieDataSource.clear()

}
