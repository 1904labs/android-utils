# android-utils ![master](https://github.com/1904labs/android-utils/workflows/master/badge.svg?branch=master)
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

## Core [![](https://img.shields.io/badge/Jitpack-core--1.0.0-brightgreen)](https://jitpack.io/#1904labs/android-utils)
The `core` module contains extension functions for Kotlin data types, as well as some helper classes around `LiveData`, AndroidX Navigation, and RxJava `Diposables`:
- LiveData
    - `KotlinLiveData` - A `LiveData` class that enforces Kotlin's nullable types
    - `KotlinMutableLiveData` - A `MutableLiveData` class that enforces Kotlin's nullable types
- AndroidX Navigation
    - `BottomNavigationBehavior` - This class acts as a bridge between the AndroidX Navigation library and a `BottomNavigationView`. It creates a separate back stack of `Fragments` for each bottom navigation tab and handles switching between these back stacks whenever the currently selected tab has changed. This allows users to switch tabs without losing their navigation state.
- RxJava
    - `DisposableUtilsKx` - Extension functions for RxJava's `Disposable` type
- Various extension functions for built-in types:
    - `BooleanUtilsKx`
    - `CollectionUtilsKx`
    - `DateUtilsKx`
    - `DoubleUtilsKx`
    - `FloatUtilsKx`
    - `NumberUtilsKx`
    - `StringUtilsKx`
    - `UtilsKx`

To include this module in your project, add the following dependency:
```
implementation 'com.github.1904labs.android-utils:core:core-1.0.0'
```

## Lint [![](https://img.shields.io/badge/Jitpack-lint--1.0.0-brightgreen)](https://jitpack.io/#1904labs/android-utils)
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

## Network [![](https://img.shields.io/badge/Jitpack-network--1.0.0-brightgreen)](https://jitpack.io/#1904labs/android-utils)
The `network` module contains two network interceptors relating to API authentication, as well as a [RFC-6265](https://tools.ietf.org/html/rfc6265#section-5.3) compliant cookie implementation:
- `AuthInterceptor`
  - An OkHttp [Interceptor](https://square.github.io/okhttp/3.x/okhttp/okhttp3/Interceptor.html) that retrieves an authentication token using `TokenDataSource.currentTokens()`, and attaches it to outgoing requests using the `Authorization` header.
- `RefreshInterceptor`
  - An OkHttp [Interceptor](https://square.github.io/okhttp/3.x/okhttp/okhttp3/Interceptor.html) that attempts to refresh expired authentication tokens. Upon receiving a 401 error, the interceptor calls `TokenApi.refreshToken()`, stores the result using `TokenDataSource.insertTokens()`, and then retries the original request using the new token.
- `PersistentCookieJar`
  - An implementation of OkHttp's [CookieJar](https://square.github.io/okhttp/3.x/okhttp/okhttp3/CookieJar.html). This implementation stores session and persistent cookies using the `SessionCookieCache` and `PersistentCookieCache` interfaces. An implementation of each are included:
    - `InMemoryCookieCache`: Stores session cookies using a `HashSet`
    - `RoomCookieCache`: A [Dao](https://developer.android.com/reference/androidx/room/Dao) for the AndroidX Room library that stores persistent cookies in a SQLite database

To include these components in your project, add the following dependency:
```
implementation 'com.github.1904labs.android-utils:network:network-1.0.0'
```

## Test-Utils [![](https://img.shields.io/badge/Jitpack-test--utils--1.0.0-brightgreen)](https://jitpack.io/#1904labs/android-utils)
The `test-utils` module contains the following to help make testing quicker and more standardized:
- Utils to generate random non-null primitive data types:
    - ```randomInt()``` - Generates a random integer and allows you to specify a range. Default range is any positive integer.
    - ```randomDouble()``` - Generates a random double and allows you to specify a range. Default range is any positive double.
    - ```randomLong()``` - Generates a random long and allows you to specify a range. Default range is any positive long.
    - ```randomFloat()``` - Generates a random float and allows you to specify a range. Default range is any positive float.
    - ```randomCharacter()``` - Generates a random character in the set (a-z, A-Z, 0-9).
    - ```randomString()``` - Generates a random string of the character set (a-z, A-Z, 0-9).
    The length of the string can be set via a min and max length. Default is a random string between 5 and 30 characters.
    - ```randomBoolean()``` - Generates a random boolean.
- Custom [Espresso](https://developer.android.com/training/testing/espresso) actions, assertions, and matchers to make
testing more efficient, concise, and readable. This library utilizes the [Barista](https://github.com/AdevintaSpain/Barista) library.
**Please make sure that any custom Espresso actions, assertions, and matchers you add to this library don't already exist within Barista.
This is meant to be an extension of that library.**
- Custom extension functions built around ```ActivityTestRule```:
    - ```rotateLandscape()``` - Easily rotate the screen into the landscape orientation.
    - ```rotatePortrait()``` - Easily rotate the screen into the portrait orientation.
    - ```launchFragment()``` - Easily launch a fragment into a dummy activity to test it in isolation.
- ```replaceNavControllerWithMock()``` - Extension function built around ```Fragment``` to easily swap
the ```NavController``` with a mock.
- ```runOnMainThread()``` - Utility function to easily run a function on the main thread.
- ```RxIdlerTestRunner``` - A fully configured test runner that utilizes [RxIdler](https://github.com/square/RxIdler)
 to help cut down on flakiness when testing code that contains asynchronous RxJava operations.
- ```TestActivity``` - A dummy activity used to test a Fragment in isolation.

To use this module in your project, add the following dependency:
```
implementation 'com.github.1904labs.android-utils:test-utils:test-utils-1.0.0'
```

## UI [![](https://img.shields.io/badge/Jitpack-ui--1.0.0-brightgreen)](https://jitpack.io/#1904labs/android-utils)
The `ui` module contains the following:
- Custom BindingAdapters:
    - ```ImageView.showNetworkImage()``` - Bind to a URL to be able to load images into the ImageView
    via Glide. This custom adapter also allows you to specify a placeholder and an error Drawable.
    - ```View.setVisible()``` - Bind to a boolean to be able to set the visibility of the view.
- Various extension functions for the following:
    - Context
    - Activity
    - Fragment
    - RecyclerView
    - TabLayout
    - View
    - ViewGroup
- Various Utils
    - ```AnimationEndListener``` - This is an interface that cuts down on some boilerplate code when trying
    to implement an AnimationListener when all you care about is wanting to know when the animation ends.
    - ```HtmlListTagHandler``` - Custom tag handler that can be passed in when converting from Html to
    a Spannable. This tag handler properly numbers ordered lists as well as properly indents both
    ordered and unordered Html lists.
    - ```LineDividerDecoration``` - This decorator can be added to a RecyclerView to draw a separator
    between the items. It can take in a custom drawable, work with vertical or horizontal orientations,
    and can hide the separator on the last item if desired.
    - ```SnapHelperOneByOne``` - This can be attached to a RecyclerView to allow the items to snap into
    place in the center one by one. This is typically used for a gallery/carousel type implementation.
    - ```ViewModelFactoryUtil``` - This utility function cuts down on some of the boilerplate code
    around creating a ViewModelProvider factory.
- Reusable Views
    - ```BaseFragment``` - A headless view class that is an extension of Fragment with a few helpers
    tacked on. This class makes setting up a toolbar, getting a reference to an AppCompatActivity,
    and getting a reference to the ActionBar a bit easier.
    - ```BinaryViewPager``` - This is a custom ViewPager that lazily loads Fragments and does not
    support swiping. Currently, it only supports a max of two Fragments.

To use this module in your project, add the following dependency:
```
implementation 'com.github.1904labs.android-utils:ui:ui-1.0.0'
```
