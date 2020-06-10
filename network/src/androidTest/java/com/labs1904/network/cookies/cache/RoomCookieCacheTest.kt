package com.labs1904.network.cookies.cache

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import com.labs1904.network.cookies.SerializableCookie
import org.junit.After
import org.junit.Before

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
}

@Database(entities = [SerializableCookie::class], version = 1)
abstract class TestDatabase : RoomDatabase() {
	abstract fun cookieEntityDao(): RoomCookieCache
}
