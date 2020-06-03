package com.labs1904.network.auth

import io.reactivex.Completable
import io.reactivex.Maybe

interface AuthTokenData<T : Token> {

	fun currentToken(): Maybe<T>

	fun insertToken(token: T): Completable

	fun clear(): Completable

}
