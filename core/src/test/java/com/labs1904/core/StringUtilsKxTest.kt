package com.labs1904.core

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class StringUtilsKxTest {

	@Test
	fun `maskFirstNChars() less than zero`() {
		val input = "username"

		val result = input.maskFirstNChars(-1)

		assertEquals(input, result)
	}

	@Test
	fun `maskFirstNChars() greater than length of string`() {
		val input = "test"

		val result = input.maskFirstNChars(5)

		assertEquals("*****", result)
	}

	@Test
	fun `maskFirstNChars() less than length of string`() {
		val input = "username"

		val result = input.maskFirstNChars(2)

		assertEquals("**ername", result)
	}

	@Test
	fun `extractInitials() behaves correctly`() {
		assertEquals(null, "".extractInitials())
		assertEquals(null, " ".extractInitials())
		assertEquals("J", "John".extractInitials())
		assertEquals("JD", "John doe".extractInitials())
		assertEquals("JH", "John david Ham".extractInitials())
	}

	@Test
	fun `decodeBase64ToByteArray() returns null on exception`() {
		val input = ""

		val result = input.decodeBase64ToByteArray()

		assertEquals(null, result)
	}

	@Test
	fun `validLength() returns false when over limit`() {
		val input = "hello"

		val result = input.validLength(maxLength = 3)

		assertEquals(false, result)
	}

	@Test
	fun `validLength() returns false when under minimum`() {
		val input = "he"

		val result = input.validLength(3, 7)

		assertEquals(false, result)
	}

	@Test
	fun `validLength() returns true`() {
		val input = "hello"

		val result = input.validLength(maxLength = 9)

		assertEquals(true, result)
	}

	@Test
	fun `parseAsDate() returns null on exception`() {
		val result = "343".parseAsDate("MM-d-YYYY")

		assertEquals(null, result)
	}

	@Test
	fun `parseAsDate() success`() {
		val result = "03-26-1994".parseAsDate("MM-dd-yyyy")

		Calendar.getInstance().apply {
			time = result!!

			assertEquals(2, get(Calendar.MONTH))
			assertEquals(26, get(Calendar.DAY_OF_MONTH))
			assertEquals(1994, get(Calendar.YEAR))
		}
	}

	@Test
	fun `toPossessiveName() handles strings that end in 's'`() {
		assertEquals("Test's", "Test".toPossessiveName())
		assertEquals("Tests'", "Tests".toPossessiveName())
	}

	@Test
	fun `defaultIfNull() returns proper values`() {
		val nullInput: String? = null

		assertEquals("value was null", nullInput.defaultIfNull("value was null"))
		assertEquals("initial", "initial".defaultIfNull("value was null"))
	}

	@Test
	fun `stringBuilder() extension function`() {
		assertEquals(StringBuilder("test").toString(), "test".stringBuilder().toString())
	}

	@Test
	fun `capitalize() extension function`() {
		assertEquals("", "".capitalize(Locale.US))
		assertEquals("Test", "Test".capitalize(Locale.US))
		assertEquals("Hello", "hello".capitalize(Locale.US))
	}

	@Test
	fun `isValidEmail() null returns false`() {
		val input: String? = null
		assertEquals(false, input.isValidEmail())
	}

	@Test
	fun `parseAsBoolean() extension function`() {
		assertEquals(true, " y ".parseAsBoolean())
		assertEquals(true, "trUE".parseAsBoolean())
		assertEquals(false, "".parseAsBoolean())
		assertEquals(false, "n".parseAsBoolean())
	}
}
