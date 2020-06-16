package com.labs1904.core

import org.junit.Assert.assertEquals
import org.junit.Test

class CollectionUtilsKxTest {

	@Test
	fun `toArrayList() returns same instance`() {
		val arrayList = arrayListOf("Test1", "Test2")

		val result = arrayList.toArrayList()

		assert(arrayList === result)
	}

	@Test
	fun `toArrayList() creates new object`() {
		val collection = listOf("Test 1", "Test 2")

		val result = collection.toArrayList()

		assert(collection !== result)
		assertEquals(arrayListOf("Test 1", "Test 2"), result)
	}

	@Test
	fun `contentEquals returns false when parameter is null`() {
		val collection = listOf("Test 1", "Test 2")

		val result = collection.contentEquals(null)

		assertEquals(false, result)
	}

	@Test
	fun `contentEquals returns false when size is different`() {
		val collection = listOf("Test 1", "Test2")

		val result = collection.contentEquals(
			listOf("Test 1")
		)

		assert(!result)
	}

	@Test
	fun `contentEquals returns true when elements are the same`() {
		val string1 = "Test 1"
		val string2 = "Test 2"
		val collection = listOf(string1, string2)

		val result = collection.contentEquals(
			listOf(string2, string1)
		)

		assert(result)
	}
}
