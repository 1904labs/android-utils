package com.labs1904.network.auth

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

interface AuthTokenDataSource<T : Token> {

	fun currentToken(): Maybe<T>

	fun insertToken(token: T): Completable

	fun clear(): Completable

}
