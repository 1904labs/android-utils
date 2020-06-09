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