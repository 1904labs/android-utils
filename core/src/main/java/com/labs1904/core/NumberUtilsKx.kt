package com.labs1904.core

import java.text.NumberFormat
import java.util.*

/**
 * Formats this Number as currency and returns a String.
 *
 * @param locale The Locale to use when formatting.
 * @param currencyCode An optional ISO-4217 currency code. If omitted, the formatter will use [locale] to determine the proper currency.
 * @return A String containing the formatted currency, or null if an exception occurred
 */
fun Number.formatCurrency(locale: Locale = Locale.getDefault(), currencyCode: String? = null): String? =
	try {
	    val numberFormat = NumberFormat.getCurrencyInstance(locale)

		if (currencyCode != null) {
			val currency = Currency.getInstance(currencyCode)

			numberFormat.currency = currency

			val defaultNumDigits = currency.defaultFractionDigits
			if (defaultNumDigits != -1) {
				val oldMinDigits = numberFormat.minimumFractionDigits

				if (oldMinDigits == numberFormat.maximumFractionDigits) {
					numberFormat.minimumFractionDigits = defaultNumDigits
					numberFormat.maximumFractionDigits = defaultNumDigits
				} else {
					numberFormat.minimumFractionDigits = defaultNumDigits.coerceAtMost(oldMinDigits)
					numberFormat.maximumFractionDigits = defaultNumDigits
				}
			}
		}

		numberFormat.format(this)
	} catch (e: Exception) { null }

/**
 * Rounds this Number to a Long, and formats it as currency (with no decimal places showing).
 *
 * @param locale The Locale to use when formatting.
 * @param currencyCode An optional ISO-4217 currency code. If omitted, the formatter will use [locale] to determine the proper currency.
 * @return A String containing the formatted currency with no decimal places, or null if an exception occurred
 */
fun Number.formatCurrencyNoDecimal(locale: Locale = Locale.getDefault(), currencyCode: String? = null): String? =
	try {
		val numberFormat = NumberFormat.getCurrencyInstance(locale)

		if (currencyCode != null) numberFormat.currency = Currency.getInstance(currencyCode)

		numberFormat.minimumFractionDigits = 0
		numberFormat.maximumFractionDigits = 0

		numberFormat.format(this.toLong())
	} catch (e: Exception) { null }

/**
 * Rounds this Number to the given [precision] and formats it as a percentage.
 *
 * @param precision The number of decimals places to display in the formatted percentage
 * @return This Number formatted as a percentage
 */
fun Number.formatPercentage(precision: Int = 0): String =
	if (precision == 0) {
		"${this.toLong()}%"
	} else {
		"%.${precision}f%%".format(this.toDouble())
	}

/**
 * Converts this Number into a String, taking into account if it represents a whole number. The output String
 * will not contain decimals if [this] is a whole number.
 *
 * @return A string representing this number, containing no decimal places if it is whole.
 */
fun Number.formatWholeNumber(): String =
	if (this.toDouble() % 1.0 == 0.0)
		this.toInt().toString()
	else
		this.toString()

/**
 * Checks whether this Number is either null or 0
 *
 * @return true if this Number is null or 0, false otherwise
 */
fun Number?.isNullOrZero(): Boolean =
	this == null || this.toDouble() == 0.0
