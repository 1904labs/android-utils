package com.labs1904.core

import java.time.ZonedDateTime

private const val SECONDS_TO_MILLIS_MULTIPLIER = 1000

/**
 * Converts this date-time to epoch milliseconds.
 *
 * @return A Long that represents the number of milliseconds from the epoch.
 */
fun ZonedDateTime.toEpochMillis(): Long = this.toEpochSecond().times(SECONDS_TO_MILLIS_MULTIPLIER)