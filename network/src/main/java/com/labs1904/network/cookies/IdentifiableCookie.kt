package com.labs1904.network.cookies

import okhttp3.Cookie

data class IdentifiableCookie(val cookie: Cookie) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as IdentifiableCookie

		if (cookie.name != other.cookie.name) return false
		if (cookie.domain != other.cookie.domain) return false
		if (cookie.path != other.cookie.path) return false

		return true
	}

	override fun hashCode(): Int {
		var result = cookie.name.hashCode()
		result = 31 * result + cookie.domain.hashCode()
		result = 31 * result + cookie.path.hashCode()
		return result
	}

}
