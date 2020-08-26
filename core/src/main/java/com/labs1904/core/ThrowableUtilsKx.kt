package com.labs1904.core

import java.io.IOException

/**
 * Any easy way to check if a Throwable is an IOException.
 *
 * @return A boolean that is true if the throwable is an IOException and false otherwise.
 */
fun Throwable.isIOException(): Boolean = this is IOException