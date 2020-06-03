package com.labs1904.network.auth

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authTokenData: AuthTokenData<*>) : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		var request = chain.request()

		if (request.headers[AUTHORIZATION] == null) {
			authTokenData.currentToken().blockingGet()?.let {
				request = request.newBuilder().addHeader(
					AUTHORIZATION,
					"$BEARER ${it.accessToken}"
				).build()
			}
		}

		return chain.proceed(request)
	}

}
