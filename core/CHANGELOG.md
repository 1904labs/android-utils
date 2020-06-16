# Changelog
All notable changes to this module will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0]
### Added
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
