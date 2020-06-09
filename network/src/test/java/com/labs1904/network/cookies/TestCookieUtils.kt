package com.labs1904.network.cookies

import okhttp3.Cookie
import okhttp3.HttpUrl.Companion.toHttpUrl

object TestCookieUtils {

	private const val DEFAULT_DOMAIN = "domain.com"
	private const val DEFAULT_PATH = "/"
	val DEFAULT_URL = "https://$DEFAULT_DOMAIN$DEFAULT_PATH".toHttpUrl()

	fun createPersistentCookie(
		name: String = "name",
		value: String = "value",
		path: String = DEFAULT_PATH,
		domain: String = DEFAULT_DOMAIN,
		expired: Boolean = false
	): Cookie = Cookie.Builder()
		.path(path)
		.name(name)
		.domain(domain)
		.value(value)
		.expiresAt(if (expired) Long.MIN_VALUE else System.currentTimeMillis() + 24 * 60 * 60 * 1000)
		.httpOnly()
		.secure()
		.build()

	fun createSessionCookie(
		name: String = "name",
		value: String = "value",
		path: String = DEFAULT_PATH,
		domain: String = DEFAULT_DOMAIN
	): Cookie = Cookie.Builder()
		.path(path)
		.name(name)
		.domain(domain)
		.value(value)
		.httpOnly()
		.secure()
		.build()
}
