package com.labs1904.core

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsKxTest {

	@Test
	fun `safeLet two arguments`() {
		val nullString: String? = null
		val returnValue = "both string non-null"

		assertEquals(null, safeLet("test", nullString, { _, _ -> }))
		assertEquals(null, safeLet(nullString, "test2", { _, _ -> }))
		assertEquals(returnValue, safeLet("test", "test2", { _, _ -> returnValue}))
	}

	@Test
	fun `safeLet three arguments`() {
		val nullString: String? = null
		val returnValue = "all strings non-null"

		assertEquals(null, safeLet("test", nullString, nullString, { _, _, _-> }))
		assertEquals(null, safeLet(nullString, nullString, "test2", { _, _, _ -> }))
		assertEquals(returnValue, safeLet("test", "test2", "test3", { _, _, _ -> returnValue}))
	}

}
