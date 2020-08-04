package com.labs1904.network.auth

/**
 * Represents a mechanism to store and retrieve an authentication token.
 */
interface TokenDataSource<T : Tokens> {

	/**
	 * Retrieve the current authentication token
	 *
	 * @return The current token if it exists, null otherwise
	 */
	fun currentTokens(): T?

	/**
	 * Insert a new authentication token
	 */
	fun insertTokens(tokens: T)

	/**
	 * Remove the current authentication token from this data source.
	 */
	fun clear()
}
