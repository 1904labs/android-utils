package com.labs1904.network.auth

import io.reactivex.rxjava3.core.Single


interface RefreshTokenApi<T : Token> {
	fun refresh(refreshToken: String): Single<T>
}
