package com.labs1904.connectivity_notifier

/**
 * An interface that allows the ability to check the availability of an individual network type (wifi, cellular, or VPN).
 */
interface NetworkState {

    val isAvailable: Boolean
}

internal class NetworkStateHolder : NetworkState {

    override var isAvailable: Boolean = false
        set(value) {
            field = value
            ConnectivityNotifier.notify()
        }
}