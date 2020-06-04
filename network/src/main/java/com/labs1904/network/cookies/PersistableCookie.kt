package com.labs1904.network.cookies

import com.labs1904.network.INVALID_EXPIRES_AT
import okhttp3.Cookie

data class PersistableCookie(
	val name: String,
	val value: String,
	val expiresAt: Long,
	val domain: String,
	val path: String,
	val secure: Boolean,
	val httpOnly: Boolean,
	val hostOnly: Boolean
) {
	constructor(cookie: Cookie) : this(
		cookie.name,
		cookie.value,
		if (cookie.persistent) cookie.expiresAt else INVALID_EXPIRES_AT,
		cookie.domain,
		cookie.path,
		cookie.secure,
		cookie.httpOnly,
		cookie.hostOnly
	)

	fun toCookie(): Cookie =
		Cookie.Builder().apply {
			name(name)
			value(value)
			if (expiresAt != INVALID_EXPIRES_AT) expiresAt(expiresAt)
			domain(domain)
			path(path)
			if (secure) secure()
			if (httpOnly) httpOnly()
			if (hostOnly) hostOnlyDomain(domain)
		}.build()
}
