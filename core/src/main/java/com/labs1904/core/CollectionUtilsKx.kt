package com.labs1904.core

/**
 * Returns an ArrayList containing the elements of this Collection.
 *
 * @return this Collection as an ArrayList
 */
fun <T: Any> Collection<T>.toArrayList(): ArrayList<T> =
	if (this is ArrayList) this else ArrayList(this)

/**
 * Checks whether the contents of two Collections are the same, regardless of ordering.
 *
 * @param collection The Collection to compare [this] to
 * @return true if [this] and [collection] are the same size, and contain the same elements (order doesn't matter), false otherwise
 */
infix fun <T: Any> Collection<T>.contentEquals(collection: Collection<T>?): Boolean =
	collection?.let {
		this.size == it.size && this.containsAll(it)
	} ?: false
