package com.labs1904.network.auth

import io.reactivex.rxjava3.core.Single

interface TokenApi<T : Token> {
	fun refreshToken(refreshToken: String): Single<T>
}
