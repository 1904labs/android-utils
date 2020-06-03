package com.labs1904.network.auth

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

interface TokenDataSource<T : Tokens> {

	fun currentTokens(): Maybe<T>

	fun insertTokens(tokens: T): Completable

	fun clear(): Completable

}
