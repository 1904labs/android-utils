package com.labs1904.core

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class DateUtilsKxTest {

	@Test
	fun `Date is formatted using the give pattern`() {
		val date = Date(1592242839148)
		val pattern = "MMM d"

		val result = date.format(pattern, Locale.ROOT)

		assertEquals("Jun 15", result)
	}

}
