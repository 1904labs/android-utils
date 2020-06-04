package com.labs1904.network.cookies.cache

import okhttp3.Cookie

interface CookieCache {
	fun loadAll(): List<Cookie>
	fun saveAll(cookies: List<Cookie>)
	fun removeAll(cookies: List<Cookie>)
	fun clear()
}
