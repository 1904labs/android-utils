package com.labs1904.network.cookies

import androidx.room.Entity
import androidx.room.Ignore
import com.labs1904.network.*
import okhttp3.Cookie

@Entity(
	tableName = COOKIES_TABLE,
	primaryKeys = [COOKIE_KEY_NAME, COOKIE_KEY_DOMAIN, COOKIE_KEY_PATH]
)
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

	@Ignore
	private var cookie: Cookie? = null

	constructor(cookie: Cookie) : this(
		cookie.name,
		cookie.value,
		if (cookie.persistent) cookie.expiresAt else INVALID_EXPIRES_AT,
		cookie.domain,
		cookie.path,
		cookie.secure,
		cookie.httpOnly,
		cookie.hostOnly
	) {
		this.cookie = cookie
	}

	fun toCookie(): Cookie =
		cookie ?: Cookie.Builder().apply {
			name(name)
			value(value)
			if (isPersistent()) expiresAt(expiresAt)
			domain(domain)
			path(path)
			if (secure) secure()
			if (httpOnly) httpOnly()
			if (hostOnly) hostOnlyDomain(domain)
		}.build().also { cookie = it }

	fun isPersistent(): Boolean =
		expiresAt != INVALID_EXPIRES_AT

	fun isExpired(): Boolean =
		expiresAt < System.currentTimeMillis()

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as PersistableCookie

		if (name != other.name) return false
		if (domain != other.domain) return false
		if (path != other.path) return false

		return true
	}

	override fun hashCode(): Int {
		var result = name.hashCode()
		result = 31 * result + domain.hashCode()
		result = 31 * result + path.hashCode()
		return result
	}
}
