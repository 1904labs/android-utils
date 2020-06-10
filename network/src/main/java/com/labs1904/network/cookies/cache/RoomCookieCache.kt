package com.labs1904.network.cookies.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.labs1904.network.COOKIES_TABLE
import com.labs1904.network.cookies.SerializableCookie

/**
 * An implementation of {@link com.labs1904.network.cookies.cache.PersistentCookieCache PersistentCookieCache}
 * that stores cookies in a Room Database. This class defines the interactions with the database.
 */
@Dao
abstract class RoomCookieCache : PersistentCookieCache {

	@Insert(onConflict = REPLACE)
	abstract override fun insert(cookie: SerializableCookie)

	@Insert(onConflict = REPLACE)
	abstract override fun insertAll(cookies: List<SerializableCookie>)

	@Delete
	abstract override fun remove(cookie: SerializableCookie)

	@Delete
	abstract override fun removeAll(cookies: List<SerializableCookie>)

	@Query("SELECT * from $COOKIES_TABLE")
	abstract override fun load(): List<SerializableCookie>

	@Query("DELETE from $COOKIES_TABLE")
	abstract override fun clear()

}
