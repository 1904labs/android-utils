package com.labs1904.network.auth

import io.reactivex.rxjava3.core.Single

interface TokenApi<T : Tokens> {
	fun refreshToken(tokens: Tokens): Single<T>
}
