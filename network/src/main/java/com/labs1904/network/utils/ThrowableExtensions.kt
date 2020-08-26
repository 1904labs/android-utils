package com.labs1904.network.utils

import retrofit2.HttpException
import javax.net.ssl.HttpsURLConnection

/**
 * Any easy way to check if a Throwable is a Retrofit HttpException with a 401 code.
 *
 * @return A boolean that is true if the throwable is a Retrofit HttpException with a 401 code and false otherwise.
 */
fun Throwable.is401Unauthorized(): Boolean = this is HttpException && this.code() == HttpsURLConnection.HTTP_UNAUTHORIZED

/**
 * Any easy way to check if a Throwable is a Retrofit HttpException with a 400 code.
 *
 * @return A boolean that is true if the throwable is a Retrofit HttpException with a 400 code and false otherwise.
 */
fun Throwable.is400BadRequest(): Boolean = this is HttpException && this.code() == HttpsURLConnection.HTTP_BAD_REQUEST