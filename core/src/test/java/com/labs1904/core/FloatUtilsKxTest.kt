package com.labs1904.core

import org.junit.Test

class FloatUtilsKxTest {

	@Test
	fun `noop when precision = 0`() {
		val testObject = 1234.4f

		val result = testObject.truncate(0)

		assert(testObject == result)
	}

	@Test
	fun `Float is not rounded`() {
		val testObject = 1.23484f

		val result = testObject.truncate(3)

		assert(1.234f == result)
	}

	@Test
	fun `Float whole number`() {
		val testObject = 4.0f

		val result = testObject.truncate(4)

		assert(4.0f == result)
	}

}
