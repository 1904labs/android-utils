package com.labs1904.core

import org.junit.Test

class DoubleUtilsKxTest {

	@Test
	fun `noop when precision = 0`() {
		val testObject = 1234.4

		val result = testObject.truncate(0)

		assert(testObject == result)
	}

	@Test
	fun `Double is not rounded`() {
		val testObject = 1.23484

		val result = testObject.truncate(3)

		assert(1.234 == result)
	}

	@Test
	fun `Double whole number`() {
		val testObject = 4.0

		val result = testObject.truncate(4)

		assert(4.0 == result)
	}

}
