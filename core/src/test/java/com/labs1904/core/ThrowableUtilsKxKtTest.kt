package com.labs1904.core

import org.junit.Assert.*
import org.junit.Test
import java.io.IOException
import java.lang.RuntimeException

class ThrowableUtilsKxKtTest {

    @Test
    fun `isIOException returns true when IOException`() {
        assertTrue(IOException().isIOException())
    }

    @Test
    fun `isIOException returns false when it is not an IOException`() {
        assertFalse(RuntimeException().isIOException())
    }
}