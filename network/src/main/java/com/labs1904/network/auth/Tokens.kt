package com.labs1904.network.auth

/**
 * The Tokens interface represents the two tokens that are generally used when interacting with
 * an authenticated API. The accessToken is attached to outgoing requests within the
 * {@link com.labs1904.network.auth.AuthInterceptor AuthInterceptor}. The refreshToken can be used
 * to call {@link com.labs1904.network.auth.TokenApi#refreshToken} to get a new accessToken.
 */
interface Tokens {
	fun getAccessToken(): String
	fun getRefreshToken(): String?
}
