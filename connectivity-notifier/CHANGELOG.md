# Changelog
All notable changes to this module will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0]
### Added
- `ConnectivityCallback`
    - A custom NetworkCallback that will update and maintain the availability of an individual network via a passed in NetworkStateHolder.
- `ConnectivityNotifier`
    - A singleton that allows for a reactive way of listening to any future connectivity changes across wifi, cellular, and VPN.
- `ConnectivityState`
    - An interface that allows for a synchronous way of fetching the current network state. This also allows the notifier to update its observable stream when any individual network's state has changed.
- `NetworkState`
    - An interface that allows the ability to check the availability of an individual network type (wifi, cellular, or VPN).