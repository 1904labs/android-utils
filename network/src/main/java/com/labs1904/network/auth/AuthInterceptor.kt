package com.labs1904.network.auth

import com.labs1904.network.AUTHORIZATION
import com.labs1904.network.BEARER
import okhttp3.Interceptor
import okhttp3.Response

/**
 * An <a href="https://square.github.io/okhttp/3.x/okhttp/okhttp3/Interceptor.html">okhttp3.Interceptor</a> that
 * retrieves an authentication token using a {@link com.labs1904.network.auth.TokenDataSource TokenDataSource}
 * and attaches it to outgoing requests using the 'Authorization' header.
 */
class AuthInterceptor(private val tokenData: TokenDataSource<*>) : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		var request = chain.request()

		if (request.header(AUTHORIZATION) == null) {
			tokenData.currentTokens().blockingGet()?.let {
				request = request.newBuilder().addHeader(
					AUTHORIZATION,
					"$BEARER ${it.accessToken}"
				).build()
			}
		}

		return chain.proceed(request)
	}

}
