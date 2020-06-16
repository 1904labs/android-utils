package com.labs1904.core

import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Truncate this Float to the specified number of decimal places
 *
 * @param precision The max number of decimal places the resulting Float should contain
 * @return This Float, truncated to [precision] number of decimal places
 */
fun Float.truncate(precision: Int): Float {
	if (precision <= 0) return this

	val pattern = "#." + "#".repeat(precision)

	return DecimalFormat(pattern).apply {
		roundingMode = RoundingMode.DOWN
	}.format(this).toFloat()
}
