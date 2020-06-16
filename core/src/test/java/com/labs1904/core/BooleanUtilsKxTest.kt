package com.labs1904.core

import org.junit.Assert.assertEquals
import org.junit.Test

class BooleanUtilsKxTest {

	@Test
	fun `then() returns value when true, null otherwise`() {
		val itemToReturn = "A string"

		assertEquals(null, false.then(itemToReturn))
		assertEquals(itemToReturn, true.then(itemToReturn))
	}

}
