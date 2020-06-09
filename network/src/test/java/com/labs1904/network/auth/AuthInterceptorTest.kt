package com.labs1904.network.auth

import com.labs1904.network.AUTHORIZATION
import com.labs1904.network.BEARER
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.core.Maybe
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthInterceptorTest {

	private lateinit var testObject: AuthInterceptor

	@Test
	fun `TokenDataSource is not accessed when Authorization header exists`() {
		val request: Request = mock()
		val expectedResponse: Response = mock()
		val chain: Interceptor.Chain = mock()
		val tokenData: TokenDataSource<Tokens> = mock()

		whenever(request.header(AUTHORIZATION)).thenReturn("$BEARER A12345667B")

		whenever(chain.request()).thenReturn(request)
		whenever(chain.proceed(request)).thenReturn(expectedResponse)

		testObject = AuthInterceptor(tokenData)

		val actualResponse = testObject.intercept(chain)

		verify(tokenData, times(0)).currentTokens()
		assertEquals(expectedResponse, actualResponse)
	}

	@Test
	fun `new request is created when Authorization header is missing`() {
		val newAccessToken = "A12345667B"

		val originalRequest: Request = mock()
		val updatedRequest: Request = mock()
		val expectedResponse: Response = mock()
		val requestBuilder: Request.Builder = mock()
		val chain: Interceptor.Chain = mock()
		val tokenData: TokenDataSource<Tokens> = mock()
		val tokenMaybe: Maybe<Tokens> = mock()
		val newTokens: Tokens = mock()

		whenever(chain.request()).thenReturn(originalRequest)

		whenever(originalRequest.header(AUTHORIZATION)).thenReturn(null)

		whenever(tokenData.currentTokens()).thenReturn(tokenMaybe)
		whenever(tokenMaybe.blockingGet()).thenReturn(newTokens)
		whenever(newTokens.accessToken).thenReturn(newAccessToken)

		whenever(originalRequest.newBuilder()).thenReturn(requestBuilder)
		whenever(requestBuilder.addHeader(AUTHORIZATION, "$BEARER $newAccessToken")).thenReturn(requestBuilder)
		whenever(requestBuilder.build()).thenReturn(updatedRequest)

		whenever(chain.proceed(updatedRequest)).thenReturn(expectedResponse)

		testObject = AuthInterceptor(tokenData)

		val actualResponse = testObject.intercept(chain)

		assertEquals(expectedResponse, actualResponse)
	}
}
