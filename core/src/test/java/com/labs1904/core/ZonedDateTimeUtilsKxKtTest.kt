package com.labs1904.core

import org.junit.Assert.*
import org.junit.Test
import java.time.ZonedDateTime

class ZonedDateTimeUtilsKxKtTest {

    @Test
    fun toEpochMillis() {
        val expected = 1577858400000L
        val input =
            ZonedDateTime.of(
                2020,
                1,
                1,
                0,
                0,
                0,
                0,
                "CST".toZoneId())

        assertEquals(expected, input.toEpochMillis())
    }
}