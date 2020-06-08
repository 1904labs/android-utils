package com.labs1904.network.cookies.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.labs1904.network.COOKIES_TABLE
import com.labs1904.network.cookies.CookieDataSource
import com.labs1904.network.cookies.PersistableCookie

@Dao
abstract class PersistentCookieCache : CookieDataSource {

	@Insert(onConflict = REPLACE)
	abstract override fun insert(cookie: PersistableCookie)

	@Insert(onConflict = REPLACE)
	abstract override fun insertAll(cookies: List<PersistableCookie>)

	@Delete
	abstract override fun remove(cookie: PersistableCookie)

	@Delete
	abstract override fun removeAll(cookies: List<PersistableCookie>)

	@Query("SELECT * from $COOKIES_TABLE")
	abstract override fun load(): List<PersistableCookie>

	@Query("DELETE from $COOKIES_TABLE")
	abstract override fun clear()

}
