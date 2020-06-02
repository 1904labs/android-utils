# android-utils
Each module in this repository is published as a library using [Jitpack](https://jitpack.io/). Jitpack makes it super easy to distribute libraries, but the way it handles versioning is not ideal for mult-module projects such as this one. Versions are defined using Git tags, which are not module-specific. This means if we create a `1.0.0` tag within the repository, Jitpack will publish a new `1.0.0` version for every module in the project. Since we want to track each module's version separately, we need to prefix our tags with the module name (e.g. `lint-1.0.0`). Eventually we will want to ditch Jitpack and publish directly to Maven Central, which would allow us to specify discrete module versions without the prefix. Publishing to Maven Central requires some additional work, such as registering an account and creating PGP keys to sign our releases.

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
- `DisposableClearDetector`
- `LiveDataDetector`
- `MutableLiveDataDetector`
- `NavControllerDetector`
