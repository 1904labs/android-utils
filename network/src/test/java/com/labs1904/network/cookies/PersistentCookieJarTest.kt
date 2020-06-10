package com.labs1904.network.cookies

import com.labs1904.network.cookies.TestCookieUtils.DEFAULT_URL
import com.labs1904.network.cookies.cache.PersistentCookieCache
import com.labs1904.network.cookies.cache.SessionCookieCache
import com.nhaarman.mockitokotlin2.*
import org.junit.Assert.assertEquals
import org.junit.Test

class PersistentCookieJarTest {

	@Test
	fun `cookies are inserted into cache`() {
		val sessionCookieCache: SessionCookieCache = mock()
		val persistentCookieCache: PersistentCookieCache = mock()
		val existingPersistentCookies = listOf(SerializableCookie(TestCookieUtils.createPersistentCookie()))
		val persistentCookieToSave = TestCookieUtils.createPersistentCookie()
		val cookiesToSave = listOf(
			TestCookieUtils.createSessionCookie(),
			persistentCookieToSave,
			TestCookieUtils.createSessionCookie()
		)

		whenever(persistentCookieCache.load()).thenReturn(existingPersistentCookies)

		val testObject = PersistentCookieJar(sessionCookieCache, persistentCookieCache)

		testObject.saveFromResponse(DEFAULT_URL, cookiesToSave)

		verify(sessionCookieCache).insertAll(existingPersistentCookies)

		val sessionCookieCaptor = argumentCaptor<SerializableCookie>()
		verify(sessionCookieCache, times(cookiesToSave.size)).insert(sessionCookieCaptor.capture())
		assertEquals(sessionCookieCaptor.allValues, cookiesToSave.map { SerializableCookie(it) })

		val persistentCookieCaptor = argumentCaptor<SerializableCookie>()
		verify(persistentCookieCache).insert(persistentCookieCaptor.capture())
		assertEquals(SerializableCookie(persistentCookieToSave), persistentCookieCaptor.firstValue)
	}

	@Test
	fun `cookies are loaded for request`() {
		val sessionCookieCache: SessionCookieCache = mock()
		val persistentCookieCache: PersistentCookieCache = mock()

		val expiredCookie = SerializableCookie(TestCookieUtils.createPersistentCookie(expired = true))
		val existingCookies = listOf(
			SerializableCookie(TestCookieUtils.createSessionCookie()),
			SerializableCookie(TestCookieUtils.createSessionCookie(domain = "anotherdomain.com")),
			SerializableCookie(TestCookieUtils.createSessionCookie()),
			expiredCookie
		)

		whenever(persistentCookieCache.load()).thenReturn(emptyList())
		whenever(sessionCookieCache.load()).thenReturn(existingCookies)

		val testObject = PersistentCookieJar(sessionCookieCache, persistentCookieCache)

		val loadedCookies = testObject.loadForRequest(DEFAULT_URL)

		assertEquals(
			listOf(existingCookies[0], existingCookies[2]).map { it.toCookie() },
			loadedCookies
		)


		verify(sessionCookieCache).removeAll(listOf(expiredCookie))
		verify(persistentCookieCache).removeAll(listOf(expiredCookie))
	}

	@Test
	fun `clear() empties both caches`() {
		val sessionCookieCache: SessionCookieCache = mock()
		val persistentCookieCache: PersistentCookieCache = mock()

		val testObject = PersistentCookieJar(sessionCookieCache, persistentCookieCache)
		testObject.clear()

		verify(sessionCookieCache).clear()
		verify(persistentCookieCache).clear()
	}

	@Test
	fun `clearSession() only clears session cache`() {
		val sessionCookieCache: SessionCookieCache = mock()
		val persistentCookieCache: PersistentCookieCache = mock()

		val existingPersistentCookies = listOf(
			SerializableCookie(TestCookieUtils.createPersistentCookie()),
			SerializableCookie(TestCookieUtils.createPersistentCookie())
		)

		whenever(persistentCookieCache.load()).thenReturn(existingPersistentCookies)

		val testObject = PersistentCookieJar(sessionCookieCache, persistentCookieCache)
		testObject.clearSession()

		verify(sessionCookieCache).clear()
		verify(sessionCookieCache).insertAll(existingPersistentCookies)

		verify(persistentCookieCache, times(0)).clear()
	}
}
