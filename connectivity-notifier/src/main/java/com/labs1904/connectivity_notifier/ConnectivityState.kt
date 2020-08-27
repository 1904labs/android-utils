package com.labs1904.connectivity_notifier

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest

/**
 * An interface that allows for a synchronous way of fetching the current network state. This also
 * allows the notifier to update its observable stream when any individual network's state has changed.
 */
interface ConnectivityState {

    /**
     * Checks if there is currently at least one available network (wifi, cellular, or VPN).
     */
    val isConnected: Boolean
        get() = networkStates.any { it.isAvailable }

    /**
     * Returns the current network states. This allows you to check a specific network type for availability.
     */
    val networkStates: Iterable<NetworkState>
}

/**
 * An implementation of ConnectivityState that is also a singleton so that if can be used globally within
 * an application as well as maintain each network's state appropriately.
 */
object ConnectivityStateHolder : ConnectivityState {

    private val mutableNetworkStates: MutableSet<NetworkState> = mutableSetOf()

    override val networkStates: Iterable<NetworkState>
        get() = mutableNetworkStates

    /**
     * An application extension function that registers the callbacks to set up network monitoring.
     */
    fun Application.registerConnectivityNetworkCallbacks() {

        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        listOf(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build(),
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build(),
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
                .build()
        ).forEach {
            val networkStateHolder = NetworkStateHolder()
            mutableNetworkStates.add(networkStateHolder)
            connectivityManager.registerNetworkCallback(
                it,
                ConnectivityCallback(networkStateHolder)
            )
        }
    }
}