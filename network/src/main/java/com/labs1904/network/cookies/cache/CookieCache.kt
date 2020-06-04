package com.labs1904.network.cookies.cache

import okhttp3.Cookie

interface CookieCache : Iterable<Cookie> {
	fun addAll(newCookies: List<Cookie>)
	fun removeAll(cookiesToRemove: List<Cookie>)
	fun clear()
}
