package com.labs1904.network.auth

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

/**
 * Represents a mechanism to store and retrieve an authentication token.
 */
interface TokenDataSource<T : Tokens> {

	/**
	 * Retrieve the current authentication token
	 *
	 * @return A Maybe that represents the operation to retrieve the current token
	 */
	fun currentTokens(): Maybe<T>

	/**
	 * Insert a new authentication token
	 *
	 * @return A Completable that will complete when the new token has been successfully stored
	 */
	fun insertTokens(tokens: T): Completable

	/**
	 * Remove the current authentication token from this data source.
	 *
	 * @return A Completable that will complete when the token is cleared
	 */
	fun clear(): Completable
}
