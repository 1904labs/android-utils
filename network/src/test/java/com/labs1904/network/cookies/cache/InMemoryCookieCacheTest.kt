package com.labs1904.network.cookies.cache

import com.labs1904.network.cookies.SerializableCookie
import com.labs1904.network.cookies.TestCookieUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class InMemoryCookieCacheTest {

	@Test
	fun `insert cookie into cache`() {
		val cookieHashSet = hashSetOf<SerializableCookie>()
		val cookieToInsert = SerializableCookie(TestCookieUtils.createSessionCookie())

		val testObject = InMemoryCookieCache(cookies = cookieHashSet)
		testObject.insert(cookieToInsert)

		assertEquals(1, cookieHashSet.size)
		assert(cookieHashSet.contains(cookieToInsert))
	}

	@Test
	fun `insert duplicate cookie into cache`() {
		val name = "testName"
		val domain = "testDomain.com"
		val path = "/test"

		val existingCookie = SerializableCookie(
			TestCookieUtils.createSessionCookie(name = name, domain = domain, path = path, value = "value1")
		)
		val cookieToInsert = SerializableCookie(
			TestCookieUtils.createSessionCookie(name = name, domain = domain, path = path, value = "value2")
		)

		val cookieHashSet = hashSetOf(existingCookie)

		val testObject = InMemoryCookieCache(cookies = cookieHashSet)
		testObject.insert(cookieToInsert)

		assertEquals(1, cookieHashSet.size)
		assert(cookieHashSet.contains(cookieToInsert))
	}

	@Test
	fun `insert multiple cookies into cache`() {
		val cookieHashSet = hashSetOf<SerializableCookie>()
		val cookiesToInsert = listOf(
			SerializableCookie(TestCookieUtils.createSessionCookie(name = "name1")),
			SerializableCookie(TestCookieUtils.createPersistentCookie(name = "name2"))
		)

		val testObject = InMemoryCookieCache(cookies = cookieHashSet)
		testObject.insertAll(cookiesToInsert)

		assertEquals(2, cookieHashSet.size)
		assert(cookieHashSet.contains(cookiesToInsert[0]))
		assert(cookieHashSet.contains(cookiesToInsert[1]))
	}

	@Test
	fun `insertAll() containing duplicate cookies`() {
		val existingCookie = SerializableCookie(TestCookieUtils.createSessionCookie(name = "name1"))
		val cookieHashSet = hashSetOf(existingCookie)
		val cookiesToInsert = listOf(
			existingCookie,
			SerializableCookie(TestCookieUtils.createPersistentCookie(name = "name2"))
		)

		val testObject = InMemoryCookieCache(cookies = cookieHashSet)
		testObject.insertAll(cookiesToInsert)

		assertEquals(2, cookieHashSet.size)
		assert(cookieHashSet.contains(existingCookie))
		assert(cookieHashSet.contains(cookiesToInsert[1]))
	}

	@Test
	fun `remove cookie from the cache`() {
		val name = "testName"
		val domain = "testDomain.com"
		val path = "/test"

		val existingCookie = SerializableCookie(
			TestCookieUtils.createSessionCookie(name = name, domain = domain, path = path, value = "value1")
		)
		val cookieToRemove = SerializableCookie(
			TestCookieUtils.createSessionCookie(name = name, domain = domain, path = path, value = "value2")
		)

		val cookieHashSet = hashSetOf(existingCookie)
		val testObject = InMemoryCookieCache(cookies = cookieHashSet)
		testObject.remove(cookieToRemove)

		assert(cookieHashSet.isEmpty())
	}

	@Test
	fun `removeAll() called with list of cookies`() {
		val existingCookies = listOf(
			SerializableCookie(TestCookieUtils.createPersistentCookie(name = "name1")),
			SerializableCookie(TestCookieUtils.createSessionCookie(name = "name2"))
		)

		val cookiesToRemove = listOf(
			SerializableCookie(TestCookieUtils.createSessionCookie(name = "name1")),
			SerializableCookie(TestCookieUtils.createPersistentCookie(name = "name2"))
		)

		val cookieHashSet = existingCookies.toHashSet()
		val testObject = InMemoryCookieCache(cookies = cookieHashSet)
		testObject.removeAll(cookiesToRemove)

		assert(cookieHashSet.isEmpty())
	}

	@Test
	fun `load() returns list of cookies`() {
		val existingCookie1 = SerializableCookie(TestCookieUtils.createPersistentCookie(name = "name1"))
		val existingCookie2 = SerializableCookie(TestCookieUtils.createSessionCookie(name = "name2"))

		val testObject = InMemoryCookieCache(cookies = hashSetOf(existingCookie1, existingCookie2))
		val result = testObject.load()

		assertEquals(2, result.size)
		assert(result.contains(existingCookie1))
		assert(result.contains(existingCookie2))
	}

	@Test
	fun `clear() empties HashSet`() {
		val existingCookies = listOf(
			SerializableCookie(TestCookieUtils.createPersistentCookie(name = "name1")),
			SerializableCookie(TestCookieUtils.createSessionCookie(name = "name2"))
		)

		val cookieHashSet = existingCookies.toHashSet()
		val testObject = InMemoryCookieCache(cookies = cookieHashSet)

		assertEquals(2, cookieHashSet.size)

		testObject.clear()

		assert(cookieHashSet.isEmpty())
	}
}
