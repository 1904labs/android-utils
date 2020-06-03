package com.labs1904.network.auth

import io.reactivex.Completable
import io.reactivex.Maybe

interface AuthTokenData {

	fun currentToken(): Maybe<String>

	fun insertToken(token: String): Completable

	fun clear(): Completable

}
