package com.labs1904.network.auth

import android.util.Log
import com.labs1904.network.AUTHORIZATION
import com.labs1904.network.BEARER
import com.labs1904.network.STATUS_CODE_401
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * An <a href="https://square.github.io/okhttp/3.x/okhttp/okhttp3/Interceptor.html">okhttp3.Interceptor</a> that
 * refreshes authentication tokens. Upon receiving a 401 error code, this interceptor calls the
 * {@link com.labs1904.network.auth.TokenApi TokenApi} to refresh the token. After successfully refreshing the token,
 * it re-attempts the original request using the new token. If the refresh fails, a logout event is emitted
 * through [logoutObservable].
 */
class RefreshInterceptor<T : Tokens>(
	private val tokenData: TokenDataSource<T>,
	private val tokenApi: TokenApi<T>,
	private val tokenHeaderName: String = AUTHORIZATION,
	private val tokenValueFormatter: (Tokens) -> String = { "$BEARER ${it.accessToken}" },
	private val logoutSubject: PublishSubject<Unit> = PublishSubject.create(),
	val logoutObservable: Observable<Unit> = logoutSubject
) : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response =
		chain.proceed(chain.request()).let { originalResponse ->
			if (originalResponse.code == STATUS_CODE_401) {
				val originalHeader = originalResponse.request.header(tokenHeaderName)
				var newRequest: Request? = null

				synchronized(this) {
					try {
						val currentTokens: Tokens? = tokenData.currentTokens().blockingGet()

						newRequest = when {
							currentTokens == null -> null
							originalHeader != tokenValueFormatter.invoke(currentTokens) -> createRequestWithNewToken(chain, currentTokens)
							else -> {
								tokenApi
									.refreshToken(currentTokens)
									.blockingGet()
									?.let {
										tokenData.insertTokens(it).blockingAwait()
										createRequestWithNewToken(chain, it)
									}
							}
						}
					} catch (e: Exception) {
						Log.e(TAG, "Refresh failed", e)
					}
				}

				newRequest?.let {
					executeRequest(chain, it, originalResponse)
				} ?: run {
					logoutSubject.onNext(Unit)
					originalResponse
				}
			} else {
				originalResponse
			}
		}

	/**
	 * Recreates the original request using the new authentication token
	 *
	 * @param chain The Interceptor.Chain that represents the initial API call
	 * @param newTokens The updated authentication and refresh tokens
	 *
	 * @return A request with the new authentication token attached
	 */
	private fun createRequestWithNewToken(chain: Interceptor.Chain, newTokens: Tokens): Request =
		chain.request().newBuilder().header(tokenHeaderName, tokenValueFormatter.invoke(newTokens)).build()

	/**
	 * Executes [request] and returns its Response.
	 *
	 * @param chain The Interceptor.Chain that represents the initial API call
	 * @param request The Request to execute
	 * @param originalResponse The failed Response that initiated this refresh logic
	 *
	 * @return The Response received by executing [request], or if it returned a 401, [originalResponse]
	 */
	private fun executeRequest(chain: Interceptor.Chain, request: Request, originalResponse: Response): Response =
		chain.proceed(request).takeIf { it.code != STATUS_CODE_401 } ?: run {
			logoutSubject.onNext(Unit)
			originalResponse
		}

	companion object {
		private const val TAG = "RefreshInterceptor"
	}
}

