package com.labs1904.network.auth

import io.reactivex.rxjava3.core.Single

/**
 * Represents an API to refresh authentication tokens. This interface can be extended to add additional
 * token related API calls.
 */
interface TokenApi<T : Tokens> {
	/**
	 * Refresh the expired authentication token
	 *
	 * @param tokens The current tokens
	 *
	 * @return A Single representing the API call. Upon success, a new Tokens object will be emitted
	 */
	fun refreshToken(tokens: Tokens): Single<T>
}
