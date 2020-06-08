# android-utils
Each module is published as a library using [Jitpack](https://jitpack.io/). Jitpack makes it easy to distribute libraries, but the way it handles versioning is not ideal for mult-module projects such as this one. Versions are defined using Git tags, which are not module-specific. This means if we create a `1.0.0` tag, Jitpack will publish a new `1.0.0` version for every module in the project. Since we want to track each module's version separately, we need to prefix our tags with the module name (e.g. `lint-1.0.0`). Eventually we will want to ditch Jitpack and publish directly to Maven Central, which would allow us to specify module versions without the prefix. Publishing to Maven Central requires some additional work, such as registering an account and creating PGP keys to sign our releases.

Before using any of the dependencies below, add the Jitpack repository to your top-level `build.gradle` file:
```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://jitpack.io' }    <--- Add this line
    }
}
```

## Lint
The `lint` module contains a few custom lint checks:
- `CompositeDisposableClearDetector`
    - A `CompositeDisposable` reference is being held, but it is not being cleared.
- `DisposableClearDetector`
    - A `Disposable` reference is being held, but it is not being disposed of.
- `LiveDataDetector`
    - `KotlinLiveData` should be used instead of `LiveData` for null safety.
- `MutableLiveDataDetector`
    - `KotlinMutableLiveData` should be used instead of `MutableLiveData` for null safety.
- `NavControllerDetector`
    - A bug exists within `NavController.navigate()` that can sometimes cause the application to crash. `NavController.navigateSafe()` should be used instead.

To include these lint checks in your project, add the following dependency:
```
implementation 'com.github.1904labs.android-utils:lint:lint-1.0.0'
```

## Network
The `network` module contains two network interceptors relating to API authentication, as well as a [RFC-6265](https://tools.ietf.org/html/rfc6265#section-5.3) compliant cookie implementation:
- `AuthInterceptor`
  - An OkHttp [Interceptor](https://square.github.io/okhttp/3.x/okhttp/okhttp3/Interceptor.html) that retrieves an authentication token using `TokenDataSource.currentTokens()`, and attaches it to outgoing requests using the `Authorization` header.
- `RefreshInterceptor`
  - An Okttp [Interceptor](https://square.github.io/okhttp/3.x/okhttp/okhttp3/Interceptor.html) that attempts to refresh expired authentication tokens. Upon receiving a 401 error, the interceptor calls `TokenApi.refreshToken()`, stores the result using `TokenDataSource.insertTokens()`, and then retries the original request using the new token.
- `PersistentCookieJar`
  - An implementation of OkHttp's [CookieJar](https://square.github.io/okhttp/3.x/okhttp/okhttp3/CookieJar.html). This implementation stores session and persistent cookies using the `SessionCookieCache` and `PersistentCookieCache` interfaces. An implementation of each are included:
    - `InMemoryCookieCache`: Stores session cookies using a `HashSet`
    - `RoomCookieCache`: A [Dao](https://developer.android.com/reference/androidx/room/Dao) for the AndroidX Room library that stores persistent cookies in a SQLite database
    
To include these components in your project, add the following dependency:
```
implementation 'com.github.1904labs.android-utils:network:network-1.0.0'
```
