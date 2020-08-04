package com.labs1904.network.auth

import com.labs1904.network.AUTHORIZATION
import com.labs1904.network.BEARER
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Maybe
import okhttp3.Interceptor
import okhttp3.Request
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthInterceptorTest {

	@Test
	fun `TokenDataSource is not accessed when Authorization header exists`() {
		val accessToken = "A12345667B"
		val url = "https://google.com/"

		val request = Request.Builder()
			.url(url)
			.header(AUTHORIZATION, "$BEARER $accessToken")
			.build()


		val chain: Interceptor.Chain = mock()
		val tokenData: TokenDataSource<Tokens> = mock()

		whenever(chain.request()).thenReturn(request)
		whenever(chain.proceed(request)).thenReturn(mock())

		AuthInterceptor(tokenData).intercept(chain)

		verify(tokenData, times(0)).currentTokens()
	}

	@Test
	fun `new request is created when Authorization header is missing`() {
		val accessToken = "A12345667B"
		val url = "https://google.com/"

		val originalRequest = Request.Builder()
			.url(url)
			.build()

		val chain: Interceptor.Chain = mock()
		val tokenData: TokenDataSource<Tokens> = mock()
		val newTokens: Tokens = mock()

		whenever(chain.request()).thenReturn(originalRequest)
		whenever(chain.proceed(any())).thenReturn(mock())

		whenever(tokenData.currentTokens()).thenReturn(Maybe.just(newTokens))
		whenever(newTokens.getAccessToken()).thenReturn(accessToken)

		AuthInterceptor(tokenData).intercept(chain)

		val requestCaptor = argumentCaptor<Request>()
		verify(chain).proceed(requestCaptor.capture())
		assertEquals("$BEARER $accessToken", requestCaptor.firstValue.header(AUTHORIZATION))
		assertEquals(url, requestCaptor.firstValue.url.toString())
	}
}
