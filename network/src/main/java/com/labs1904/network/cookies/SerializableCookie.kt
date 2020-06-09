package com.labs1904.network.cookies

import androidx.room.Entity
import androidx.room.Ignore
import com.labs1904.network.COOKIES_TABLE
import com.labs1904.network.COOKIE_KEY_DOMAIN
import com.labs1904.network.COOKIE_KEY_NAME
import com.labs1904.network.COOKIE_KEY_PATH
import okhttp3.Cookie

/**
 * A wrapper class around <a href="https://square.github.io/okhttp/4.x/okhttp/okhttp3/-cookie/">okhttp3.Cookie</a>
 * used to store a cookie in either a Room DB, or an in-memory data structure such as a <a href="https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/">Set</a>.
 *
 * According to RFC-6265, cookies are considered equal if they have the same name, domain, and path. To prevent duplicate
 * cookies within the Room DB table, a composite key is defined using these three properties. Additionally, the
 * {@link #equals} function has been overridden to ensure two instances of SerializableCookie are considered equal
 * if their name, domain, and path properties are equal.
 */
@Entity(
	tableName = COOKIES_TABLE,
	primaryKeys = [COOKIE_KEY_NAME, COOKIE_KEY_DOMAIN, COOKIE_KEY_PATH]
)
data class SerializableCookie(
	val name: String,
	val value: String,
	val expiresAt: Long,
	val domain: String,
	val path: String,
	val secure: Boolean,
	val httpOnly: Boolean,
	val persistent: Boolean,
	val hostOnly: Boolean
) {

	/**
	 * Rather than instantiating an <a href="https://square.github.io/okhttp/4.x/okhttp/okhttp3/-cookie/">okhttp3.Cookie</a>
	 * each time {@link #toCookie()} is called, we cache an instance of it here.
	 */
	@Ignore
	private var cookie: Cookie? = null

	constructor(cookie: Cookie) : this(
		cookie.name,
		cookie.value,
		cookie.expiresAt,
		cookie.domain,
		cookie.path,
		cookie.secure,
		cookie.httpOnly,
		cookie.persistent,
		cookie.hostOnly
	) {
		this.cookie = cookie
	}

	/**
	 * Constructs an <a href="https://square.github.io/okhttp/4.x/okhttp/okhttp3/-cookie/">okhttp3.Cookie</a>
	 * using the data stored within this SerializableCookie.
	 *
	 * @return An <a href="https://square.github.io/okhttp/4.x/okhttp/okhttp3/-cookie/">okhttp3.Cookie</a>
	 */
	fun toCookie(): Cookie =
		cookie ?: Cookie.Builder().apply {
			name(name)
			value(value)
			if (persistent) expiresAt(expiresAt)
			domain(domain)
			path(path)
			if (secure) secure()
			if (httpOnly) httpOnly()
			if (hostOnly) hostOnlyDomain(domain)
		}.build().also { cookie = it }

	/**
	 * Convenience function to check whether this cookie has expired
	 *
	 * @return true if this cookie has expired, false otherwise
	 */
	fun isExpired(): Boolean =
		expiresAt < System.currentTimeMillis()

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as SerializableCookie

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
