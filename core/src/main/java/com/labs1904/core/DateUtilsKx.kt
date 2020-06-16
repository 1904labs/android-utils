package com.labs1904.core

import java.text.SimpleDateFormat
import java.util.*

/**
 * Format this Date into a String using the given [datePattern]
 *
 * @param datePattern the pattern describing the date and time format. See {@link java.text.SimpleDateFormat} for more information
 * @param locale the locale whose date format symbols should be used
 * @return this Date formatted as a String
 */
fun Date.format(datePattern: String, locale: Locale = Locale.getDefault()): String? =
	try {
	    SimpleDateFormat(datePattern, locale).format(this)
	} catch (e: Exception) { null }
