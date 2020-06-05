package com.labs1904.network.cookies

interface CookieDataSource {
	fun insert(cookie: PersistableCookie)
	fun insertAll(cookies: List<PersistableCookie>)
	fun remove(cookie: PersistableCookie)
	fun removeAll(cookies: List<PersistableCookie>)
	fun load(): List<PersistableCookie>
	fun clear()
}
