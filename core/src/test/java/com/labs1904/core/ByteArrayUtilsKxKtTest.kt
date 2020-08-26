package com.labs1904.core

import org.junit.Assert.assertNull
import org.junit.Test

class ByteArrayUtilsKxKtTest {

    @Test
    fun `toBase64String() returns null on exception`() {
        val input: ByteArray? = null

        val result = input.toBase64String()

        assertNull(result)
    }
}