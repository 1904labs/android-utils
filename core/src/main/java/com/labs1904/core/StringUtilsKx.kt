package com.labs1904.core

import android.util.Base64
import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.*

private val whitespace: Regex by lazy { "\\s+".toRegex() }

/**
 * Mask the first n characters of this String using the given [maskChar].
 *
 * <pre>
 * "username".maskFirstNChars(3, '%') == "%%%rname"
 * </pre>
 *
 * @param numMaskedChars The number of characters to mask, beginning at the start of this String
 * @param maskChar The character used to mask this String
 * @return This String, with its first n characters replaced with [maskChar]
 */
fun String.maskFirstNChars(numMaskedChars: Int, maskChar: Char = '*'): String =
	maskChar.toString().repeat(numMaskedChars).let { maskedSection ->
		when {
			numMaskedChars == 0 -> this
			this.length <= numMaskedChars -> maskedSection
			else -> this.replaceRange(0 until numMaskedChars, maskedSection)
		}
	}

/**
 * Given this String represents a person's name, this function attempts to extract their initials.
 *
 * <pre>
 * "".extractInitials() == null
 * " ".extractInitials() == null
 * "John".extractInitials() == "J"
 * "John doe".extractInitials() == "JD"
 * "tom jacob doe".extractInitials() == "TD"
 * </pre>
 *
 * @param locale The Locale to use when capitalizing characters
 * @return A String containing the initials, or null if the String is empty or only contains whitespace
 */
fun String.extractInitials(locale: Locale = Locale.getDefault()): String? =
	this.trim().split(whitespace).mapNotNull { it.firstOrNull() }.takeIf { it.isNotEmpty() }?.let { splitString ->
		when (splitString.size) {
			1 -> splitString.first().toString()
			else -> "${splitString.first()} ${splitString.last()}"
		}
	}?.toUpperCase(locale)

/**
 * Decodes this Base64 String into a ByteArray.
 *
 * @param flags Controls certain features of the decoded output. See {@link android.util.Base64} for more information
 * @return A ByteArray of the decoded data, or null if an exception occurred.
 */
fun String.decodeBase64ToByteArray(flags: Int = Base64.DEFAULT): ByteArray? =
	try {
		Base64.decode(this, flags)
	} catch (e: Exception) {
		null
	}

/**
 * Checks whether this String is a valid length.
 *
 * @param minLength The minimum length this String can be before becoming invalid
 * @param maxLength The maximum length this String can be before becoming invalid
 * @return true if this String's length is greater than or equal to [minLength] and less than or equal to [maxLength], false otherwise.
 */
fun String.validLength(minLength: Int = 0, maxLength: Int): Boolean = this.length in minLength..maxLength

/**
 * Parses this String as a Date using the given [datePattern].
 *
 * @param datePattern the pattern describing the date and time format. See {@link java.text.SimpleDateFormat} for more information
 * @param locale the locale whose date format symbols should be used
 * @return The parsed Date, or null if an error occurred
 */
fun String.parseAsDate(datePattern: String, locale: Locale = Locale.getDefault()): Date? =
	try {
		SimpleDateFormat(datePattern, locale).parse(this)
	} catch (e: Exception) { null }

/**
 * Given this String represents a person's name, convert the name to its possessive form.
 *
 * @return the possessive form of this String
 */
fun String.toPossessiveName(): String =
	if (this.endsWith('s', ignoreCase = true))
		"$this'"
	else
		"$this's"

/**
 * Returns [defaultValue] if this String is null
 *
 * @param defaultValue The value to return if [this] is null
 * @return this String, or if it's null, [defaultValue]
 */
fun String?.defaultIfNull(defaultValue: String = ""): String = this ?: defaultValue

/**
 * Returns a {@link kotlin.text.StringBuilder} using [this] as the initial content
 *
 * @return a StringBuilder with [this] as the initial content
 */
fun String.stringBuilder(): StringBuilder = StringBuilder(this)

/**
 * Note: There is an existing String.capitalize(locale: Locale), but it is experimental, so we have
 * pulled in our own.
 *
 * Returns a copy of this string having its first letter titlecased preferring [Char.toTitleCase] (if different from
 * [Char.toUpperCase]) or by [String.toUpperCase] using the specified [locale], or the original string,
 * if it's empty or already starts with an upper case letter.
 *
 * @param locale use the case transformation rules for this locale
 * @return This String with its first character capitalized
 */
fun String.capitalize(locale: Locale = Locale.getDefault()): String {
	if (isNotEmpty()) {
		val firstChar = this[0]
		if (firstChar.isLowerCase()) {
			return buildString {
				val titleChar = firstChar.toTitleCase()
				if (titleChar != firstChar.toUpperCase()) {
					append(titleChar)
				} else {
					append(this@capitalize.substring(0, 1).toUpperCase(locale))
				}
				append(this@capitalize.substring(1))
			}
		}
	}
	return this
}

/**
 * Checks whether this string represents a valid email.
 *
 * @return true if this String is a valid email, false otherwise
 */
fun String?.isValidEmail(): Boolean =
	if (this == null) false
	else Patterns.EMAIL_ADDRESS.matcher(this).matches()

/**
 * Parses this String as a Boolean
 *
 * @return true if this String equals 'y' or 'true', false otherwise (case is ignored)
 */
fun String.parseAsBoolean(): Boolean =
	this.trim().let {
		it.equals("y", true) || it.equals("true", true)
	}
