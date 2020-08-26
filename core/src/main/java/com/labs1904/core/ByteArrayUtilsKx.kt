package com.labs1904.core

import android.util.Base64

/**
 * Safe and simple way to convert a ByteArray into a Base64 encoded string. This function
 * catches all exceptions and returns null if one is encountered.
 *
 * @param flags (defaults to Base64.DEFAULT) Controls certain features of the encoded output. Passing {@code DEFAULT} results in output that adheres to RFC 2045.
 * @return The base64 encoded string or null if an exception occurred.
 */
fun ByteArray?.toBase64String(flags: Int = Base64.DEFAULT): String? =
    try {
        Base64.encodeToString(this, flags)
    } catch (e: Exception) {
        null
    }