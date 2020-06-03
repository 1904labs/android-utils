package com.labs1904.network.auth

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authTokenData: AuthTokenData) : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		var request = chain.request()

		if (request.headers[AUTHORIZATION] == null) {
			authTokenData.currentToken().blockingGet()?.let { token ->
				request = request.newBuilder().addHeader(
					AUTHORIZATION,
					"$BEARER $token"
				).build()
			}
		}

		return chain.proceed(request)
	}

	companion object {
		private const val AUTHORIZATION = "Authorization"
		private const val BEARER = "Bearer"
	}
}
