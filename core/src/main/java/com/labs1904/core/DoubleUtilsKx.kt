package com.labs1904.core

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Truncate this Double to the specified number of decimal places
 *
 * @param precision The max number of decimal places the resulting Double should contain
 * @return This Double, truncated to [precision] number of decimal places
 */
fun Double.truncate(precision: Int): Double {
	if (precision <= 0) return this

	val pattern = "#." + "#".repeat(precision)

	return DecimalFormat(pattern).apply {
		roundingMode = RoundingMode.DOWN
	}.format(this).toDouble()
}
