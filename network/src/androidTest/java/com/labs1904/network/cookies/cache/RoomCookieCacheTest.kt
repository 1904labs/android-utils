package com.labs1904.network.cookies.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import com.labs1904.network.cookies.SerializableCookie
import com.labs1904.network.cookies.TestCookieUtils
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RoomCookieCacheTest {

	private lateinit var cookieDao: RoomCookieCache
	private lateinit var testDatabase: TestDatabase

	@Before
	fun createDb() {
		val context = ApplicationProvider.getApplicationContext<Context>()
		testDatabase = Room.inMemoryDatabaseBuilder(context, TestDatabase::class.java).build()
		cookieDao = testDatabase.cookieEntityDao()
	}

	@After
	fun closeDb() {
		testDatabase.close()
	}

	@Test
	fun insert_cookie() {
		val cookieToInsert = TestCookieUtils.createPersistentCookie(value = "value1")

		cookieDao.insert(SerializableCookie(cookieToInsert))

		val cookiesInDB = cookieDao.load()

		assertEquals(1, cookiesInDB.size)
		assertEquals(cookieToInsert, cookiesInDB.first().toCookie())
	}

	@Test
	fun overwrite_existing_cookie() {
		val initialCookie = TestCookieUtils.createPersistentCookie(value = "value1")

		cookieDao.insert(SerializableCookie(initialCookie))

		var cookiesInDB = cookieDao.load()

		assertEquals(1, cookiesInDB.size)
		assertEquals(initialCookie, cookiesInDB.first().toCookie())

		val newCookie = TestCookieUtils.createPersistentCookie(value = "value2")

		cookieDao.insert(SerializableCookie(newCookie))

		cookiesInDB = cookieDao.load()

		assertEquals(1, cookiesInDB.size)
		assertEquals(newCookie, cookiesInDB.first().toCookie())
	}

	@Test
	fun insert_multiple_cookies() {
		val cookiesToInsert = listOf(
			TestCookieUtils.createPersistentCookie(name = "name1"),
			TestCookieUtils.createPersistentCookie(name = "name2", domain = "anotherdomain.com"),
			TestCookieUtils.createPersistentCookie(name = "name3", path = "/path")
		)

		cookieDao.insertAll(cookiesToInsert.map { SerializableCookie(it) })

		val cookiesInDb = cookieDao.load()

		assertEquals(3, cookiesInDb.size)
		assertEquals(cookiesToInsert, cookiesInDb.map { it.toCookie() })
	}

	@Test
	fun insert_multiple_existing_cookies() {
		val initialListOfCookies = listOf(
			TestCookieUtils.createPersistentCookie(name = "name1"),
			TestCookieUtils.createPersistentCookie(name = "name2", domain = "anotherdomain.com"),
			TestCookieUtils.createPersistentCookie(name = "name3", path = "/path")
		)

		cookieDao.insertAll(initialListOfCookies.map { SerializableCookie(it) })

		var cookiesInDb = cookieDao.load()

		assertEquals(3, cookiesInDb.size)
		assertEquals(initialListOfCookies, cookiesInDb.map { it.toCookie() })

		val newCookies = listOf(
			TestCookieUtils.createPersistentCookie(name = "name1", value = "newValue"),
			TestCookieUtils.createPersistentCookie(name = "name3", value = "anotherNewValue", path = "/path")
		)

		cookieDao.insertAll(newCookies.map { SerializableCookie(it) })

		cookiesInDb = cookieDao.load()

		assertEquals(3, cookiesInDb.size)
		assertEquals(
			listOf(initialListOfCookies[1], newCookies[0], newCookies[1]),
			cookiesInDb.map { it.toCookie() }
		)
	}

	@Test
	fun remove_cookie() {
		val initialListOfCookies = listOf(
			TestCookieUtils.createPersistentCookie(name = "name1"),
			TestCookieUtils.createPersistentCookie(name = "name2"),
			TestCookieUtils.createPersistentCookie(name = "name3")
		)

		cookieDao.insertAll(initialListOfCookies.map { SerializableCookie(it) })

		var cookiesInDb = cookieDao.load()

		assertEquals(3, cookiesInDb.size)
		assertEquals(initialListOfCookies, cookiesInDb.map { it.toCookie() })

		val cookieToRemove = TestCookieUtils.createPersistentCookie("name2")

		cookieDao.remove(SerializableCookie(cookieToRemove))

		cookiesInDb = cookieDao.load()

		assertEquals(2, cookiesInDb.size)
		assertEquals(
			listOf(initialListOfCookies[0], initialListOfCookies[2]),
			cookiesInDb.map { it.toCookie() }
		)
	}

	@Test
	fun remove_multiple_cookies() {
		val initialListOfCookies = listOf(
			TestCookieUtils.createPersistentCookie(name = "name1"),
			TestCookieUtils.createPersistentCookie(name = "name2"),
			TestCookieUtils.createPersistentCookie(name = "name3")
		)

		cookieDao.insertAll(initialListOfCookies.map { SerializableCookie(it) })

		var cookiesInDb = cookieDao.load()

		assertEquals(3, cookiesInDb.size)
		assertEquals(initialListOfCookies, cookiesInDb.map { it.toCookie() })

		val cookiesToRemove = listOf(
			TestCookieUtils.createPersistentCookie(name = "name1"),
			TestCookieUtils.createPersistentCookie(name = "name2")
		)

		cookieDao.removeAll(cookiesToRemove.map { SerializableCookie(it) })

		cookiesInDb = cookieDao.load()

		assertEquals(1, cookiesInDb.size)
		assertEquals(initialListOfCookies[2], cookiesInDb.first().toCookie())
	}

	@Test
	fun clear_all_cookies() {
		val initialListOfCookies = listOf(
			TestCookieUtils.createPersistentCookie(name = "name1"),
			TestCookieUtils.createPersistentCookie(name = "name2"),
			TestCookieUtils.createPersistentCookie(name = "name3")
		)

		cookieDao.insertAll(initialListOfCookies.map { SerializableCookie(it) })

		var cookiesInDb = cookieDao.load()

		assertEquals(3, cookiesInDb.size)
		assertEquals(initialListOfCookies, cookiesInDb.map { it.toCookie() })

		cookieDao.clear()

		cookiesInDb = cookieDao.load()

		assert(cookiesInDb.isEmpty())
	}
}

@Database(entities = [SerializableCookie::class], version = 1)
abstract class TestDatabase : RoomDatabase() {
	abstract fun cookieEntityDao(): RoomCookieCache
}
