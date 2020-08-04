package com.labs1904.network.auth

import com.labs1904.network.AUTHORIZATION
import com.labs1904.network.BEARER
import com.labs1904.network.STATUS_CODE_401
import com.nhaarman.mockitokotlin2.*
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Test

class RefreshInterceptorTest {

	@Test
	fun `non 401 response codes proceed with no token refresh`() {
		val tokenData: TokenDataSource<Tokens> = mock()
		val tokenApi: TokenApi<Tokens> = mock()
		val chain: Interceptor.Chain = mock()
		val request = Request.Builder().url("https://google.com/").build()
		val originalResponse: Response = mock()

		whenever(chain.request()).thenReturn(request)
		whenever(chain.proceed(request)).thenReturn(originalResponse)

		whenever(originalResponse.code).thenReturn(200)

		val testObject = RefreshInterceptor(tokenData, tokenApi)
		val testObserver = testObject.logoutObservable.test()

		val actualResponse = testObject.intercept(chain)

		testObserver.apply {
			assertNoValues()
			assertNoErrors()
			assertNotComplete()
		}

		assertEquals(originalResponse, actualResponse)
		verify(originalResponse, times(0)).request
	}

	@Test
	fun `currentToken=null triggers logout event & returns originalResponse`() {
		val tokenData: TokenDataSource<Tokens> = mock()
		val tokenApi: TokenApi<Tokens> = mock()
		val chain: Interceptor.Chain = mock()
		val request = Request.Builder()
			.url("https://google.com/")
			.header(AUTHORIZATION, "$BEARER 1234556")
			.build()
		val originalResponse = Response.Builder()
			.request(request)
			.code(STATUS_CODE_401)
			.protocol(Protocol.HTTP_1_1)
			.message("Error")
			.build()

		whenever(chain.request()).thenReturn(request)
		whenever(chain.proceed(request)).thenReturn(originalResponse)

		whenever(tokenData.currentTokens()).thenReturn(Maybe.empty())

		val testObject = RefreshInterceptor(tokenData, tokenApi)
		val testObserver = testObject.logoutObservable.test()

		val actualResponse = testObject.intercept(chain)

		testObserver.apply {
			assertValue(Unit)
			assertNotComplete()
			assertNoErrors()
		}

		assertEquals(originalResponse, actualResponse)
	}

	@Test
	fun `token was refreshed by a different thread`() {
		val originalAccessToken = "1234556"
		val updatedAccessToken = "43434343"

		val updatedTokens: Tokens = mock()
		val tokenData: TokenDataSource<Tokens> = mock()
		val tokenApi: TokenApi<Tokens> = mock()
		val chain: Interceptor.Chain = mock()
		val request = Request.Builder()
			.url("https://google.com/")
			.header(AUTHORIZATION, "$BEARER $originalAccessToken")
			.build()
		val originalResponse = Response.Builder()
			.request(request)
			.code(STATUS_CODE_401)
			.protocol(Protocol.HTTP_1_1)
			.message("Error")
			.build()

		val successResponse: Response = mock()
		whenever(successResponse.code).thenReturn(200)

		whenever(chain.request()).thenReturn(request)
		whenever(chain.proceed(any())).thenReturn(originalResponse, successResponse)

		whenever(updatedTokens.getAccessToken()).thenReturn(updatedAccessToken)
		whenever(tokenData.currentTokens()).thenReturn(Maybe.just(updatedTokens))

		val testObject = RefreshInterceptor(tokenData, tokenApi)
		val testObserver = testObject.logoutObservable.test()

		val actualResponse = testObject.intercept(chain)

		testObserver.apply {
			assertNoValues()
			assertNoErrors()
			assertNotComplete()
		}

		assertEquals(successResponse, actualResponse)

		val requestCaptor = argumentCaptor<Request>()
		verify(chain, times(2)).proceed(requestCaptor.capture())

		assertEquals("$BEARER $updatedAccessToken", requestCaptor.secondValue.header(AUTHORIZATION))
	}

	@Test
	fun `request containing updated token fails with another 401`() {
		val originalAccessToken = "1234556"
		val updatedAccessToken = "43434343"

		val updatedTokens: Tokens = mock()
		val tokenData: TokenDataSource<Tokens> = mock()
		val tokenApi: TokenApi<Tokens> = mock()
		val chain: Interceptor.Chain = mock()
		val request = Request.Builder()
			.url("https://google.com/")
			.header(AUTHORIZATION, "$BEARER $originalAccessToken")
			.build()
		val originalResponse = Response.Builder()
			.request(request)
			.code(STATUS_CODE_401)
			.protocol(Protocol.HTTP_1_1)
			.message("Error")
			.build()

		val failedResponse: Response = mock()
		whenever(failedResponse.code).thenReturn(STATUS_CODE_401)

		whenever(chain.request()).thenReturn(request)
		whenever(chain.proceed(any())).thenReturn(originalResponse, failedResponse)

		whenever(updatedTokens.getAccessToken()).thenReturn(updatedAccessToken)
		whenever(tokenData.currentTokens()).thenReturn(Maybe.just(updatedTokens))

		val testObject = RefreshInterceptor(tokenData, tokenApi)
		val testObserver = testObject.logoutObservable.test()

		val actualResponse = testObject.intercept(chain)

		testObserver.apply {
			assertValue(Unit)
			assertNotComplete()
			assertNoErrors()
		}

		assertEquals(originalResponse, actualResponse)
	}

	@Test
	fun `token is successfully refreshed`() {
		val originalAccessToken = "1234556"
		val updatedAccessToken = "43434343"

		val currentTokens: Tokens = mock()
		val updatedTokens: Tokens = mock()
		val tokenData: TokenDataSource<Tokens> = mock()
		val tokenApi: TokenApi<Tokens> = mock()
		val chain: Interceptor.Chain = mock()
		val request = Request.Builder()
			.url("https://google.com/")
			.header(AUTHORIZATION, "$BEARER $originalAccessToken")
			.build()
		val originalResponse = Response.Builder()
			.request(request)
			.code(STATUS_CODE_401)
			.protocol(Protocol.HTTP_1_1)
			.message("Error")
			.build()

		val successResponse: Response = mock()
		whenever(successResponse.code).thenReturn(200)

		whenever(chain.request()).thenReturn(request)
		whenever(chain.proceed(any())).thenReturn(originalResponse, successResponse)

		whenever(currentTokens.getAccessToken()).thenReturn(originalAccessToken)
		whenever(updatedTokens.getAccessToken()).thenReturn(updatedAccessToken)

		whenever(tokenData.currentTokens()).thenReturn(Maybe.just(currentTokens))
		whenever(tokenData.insertTokens(updatedTokens)).thenReturn(Completable.complete())

		whenever(tokenApi.refreshToken(currentTokens)).thenReturn(Single.just(updatedTokens))

		val testObject = RefreshInterceptor(tokenData, tokenApi)
		val testObserver = testObject.logoutObservable.test()

		val actualResponse = testObject.intercept(chain)

		testObserver.apply {
			assertNoValues()
			assertNoErrors()
			assertNotComplete()
		}

		assertEquals(successResponse, actualResponse)

		val requestCaptor = argumentCaptor<Request>()
		verify(chain, times(2)).proceed(requestCaptor.capture())

		assertEquals("$BEARER $updatedAccessToken", requestCaptor.secondValue.header(AUTHORIZATION))

		verify(tokenData).insertTokens(updatedTokens)
		verify(tokenApi).refreshToken(currentTokens)
	}
}
