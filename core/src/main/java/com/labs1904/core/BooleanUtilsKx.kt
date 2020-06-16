package com.labs1904.core

/**
 * Return [value] if this Boolean is true, null otherwise
 *
 * @param value The value to return if this Boolean is true
 * @return [value] if this Boolean is true, null otherwise
 */
infix fun <T> Boolean.then(value: T): T? = if (this) value else null
