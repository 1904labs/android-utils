package com.labs1904.network.cookies

interface CookieDataSource {
	fun insert(cookies: List<PersistableCookie>)
	fun remove(cookies: List<PersistableCookie>)
	fun load(): List<PersistableCookie>
	fun clear()
}
