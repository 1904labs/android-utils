package com.labs1904.core.rx

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.rxjava3.disposables.Disposable
import org.junit.Assert.assertEquals
import org.junit.Test

class DisposableUtilsKxTest {

	@Test
	fun `isNullOrDisposed() returns true when null`() {
		val input: Disposable? = null

		val result = input.isNullOrDisposed()

		assertEquals(true, result)
	}

	@Test
	fun `isNullOrDisposed() returns true when disposed`() {
		val input: Disposable = mock()

		whenever(input.isDisposed).thenReturn(true)

		val result = input.isNullOrDisposed()

		assertEquals(true, result)
	}

	@Test
	fun `isNullOrDisposed returns false`() {
		val input: Disposable = mock()

		whenever(input.isDisposed).thenReturn(false)

		val result = input.isNullOrDisposed()

		assertEquals(false, result)
	}
}
