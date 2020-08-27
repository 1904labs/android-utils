package com.labs1904.connectivity_notifier

import android.net.ConnectivityManager
import android.net.Network

/**
 * A custom NetworkCallback that will update and maintain the availability of an individual network via
 * a passed in NetworkStateHolder.
 */
internal class ConnectivityCallback(private val networkState: NetworkStateHolder) : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        networkState.isAvailable = true
    }

    override fun onLost(network: Network) {
        networkState.isAvailable = false
    }
}