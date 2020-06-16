package com.labs1904.core

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class NumberUtilsKxTest {

	@Test
	fun `formatCurrency() returns null on exception`() {
		val input = 123.0

		val result = input.formatCurrency(Locale.US, "xxxxx")

		assertEquals(null, result)
	}

	@Test
	fun `formatCurrency() with null currencyCode`() {
		val input = 123.909

		val result = input.formatCurrency(Locale.US, null)

		assertEquals("$123.91", result)
	}

	@Test
	fun `formatCurrency() custom currencyCode`() {
		val input = 1234.999

		val result = input.formatCurrency(Locale.US, "EUR")

		assertEquals("EUR1,235.00", result)
	}

	@Test
	fun `formatCurrencyNoDecimal() returns null on exception`() {
		val input = 12345.80

		val result = input.formatCurrencyNoDecimal(Locale.US, "xxxxx")

		assertEquals(null, result)
	}

	@Test
	fun `formatCurrencyNoDecimal() with null currencyCode`() {
		val input = 12345.80

		val result = input.formatCurrencyNoDecimal(Locale.US, null)

		assertEquals("$12,346", result)
	}

	@Test
	fun `formatCurrencyNoDecimal() custom currencyCode`() {
		val input = 456.1

		val result = input.formatCurrencyNoDecimal(Locale.US, "EUR")

		assertEquals("EUR456", result)
	}

	@Test
	fun `formatPercentage() with precision = 0`() {
		val input = 98.9

		val result = input.formatPercentage(0)

		assertEquals("99%", result)
	}

	@Test
	fun `formatPercentage() with precision greater than 0`() {
		val input = 56.349

		val result = input.formatPercentage(2)

		assertEquals("56.35%", result)
	}

	@Test
	fun `formatWholeNumber() doesn't output decimals when whole number`() {
		val input = 99.0

		val result = input.formatWholeNumber()

		assertEquals("99", result)
	}

	@Test
	fun `formatWholeNumber() output decimals`() {
		val input = 33.3

		val result = input.formatWholeNumber()

		assertEquals("33.3", result)
	}

	@Test
	fun `isNullOrZero returns false`() {
		val input = 0.1

		val result = input.isNullOrZero()

		assertEquals(false, result)
	}

	@Test
	fun `isNullOrZero returns true when null`() {
		val input: Number? = null

		val result = input.isNullOrZero()

		assertEquals(true, result)
	}

	@Test
	fun `isNullOrZero returns true when zero`() {
		val input = 0.0000

		val result = input.isNullOrZero()

		assertEquals(true, result)
	}
}
