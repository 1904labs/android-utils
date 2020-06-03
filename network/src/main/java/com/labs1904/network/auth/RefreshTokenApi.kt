package com.labs1904.network.auth

import io.reactivex.Single

interface RefreshTokenApi<T : Token> {
	fun refresh(refreshToken: String): Single<T>
}
