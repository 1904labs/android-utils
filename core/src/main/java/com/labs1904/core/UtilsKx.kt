package com.labs1904.core

/**
 * If both [p1] and [p2] are not null, execute [block] and return its result. Otherwise return null.
 *
 * @param p1 The first value required to be non-null
 * @param p2 The second value required to be non-null
 * @return The result of [block] if both [p1] and [p2] are not null. If at least one parameter is null, this function returns null
 */
fun <T1 : Any, T2: Any, R: Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? =
	if (p1 != null && p2 != null) block(p1, p2) else null

/**
 * If [p1], [p2], and [p3] are not null, execute [block] and return its result. Otherwise return null.
 *
 * @param p1 The first value required to be non-null
 * @param p2 The second value required to be non-null
 * @param p3 The third value required to be non-null
 * @return The result of [block] if [p1], [p2], and [p3] are not null. If at least one parameter is null, this function returns null
 */
fun <T1 : Any, T2: Any, T3: Any, R: Any> safeLet(p1: T1?, p2: T2?, p3: T3?, block: (T1, T2, T3) -> R?): R? =
	if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
