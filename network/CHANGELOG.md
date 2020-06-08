# Changelog
All notable changes to this module will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0]
### Added
- `AuthInterceptor`
  - An OkHttp [Interceptor](https://square.github.io/okhttp/3.x/okhttp/okhttp3/Interceptor.html) that retrieves an authentication token with `TokenDataSource.currentTokens()`, and attaches it to outgoing requests using the `Authorization` header.
- `RefreshInterceptor`
  - An Okttp [Interceptor](https://square.github.io/okhttp/3.x/okhttp/okhttp3/Interceptor.html) that attempts to refresh expired authentication tokens. Upon receiving a 401 error, the interceptor calls `TokenApi.refreshToken()`, stores the result using `TokenDataSource.insertTokens()`, and then retries the original request using the new token.
- `PersistentCookieJar`
  - An implementation of OkHttp's [CookieJar](https://square.github.io/okhttp/3.x/okhttp/okhttp3/CookieJar.html). This implementation stores both session and persistent cookies using the `CookieDataSource` interface. Two implementations of `CookieDataSource` are included:
    - `SessionCookieCache`: Stores cookies in-memory using a `HashSet`
    - `PersistentCookieCache`: A [Dao](https://developer.android.com/reference/androidx/room/Dao) for the AndroidX Room library that stores cookies in a SQLite database
