package com.labs1904.connectivity_notifier

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject

/**
 * A singleton that allows for a reactive way of listening to any future connectivity changes across
 * wifi, cellular, and VPN.
 */
object ConnectivityNotifier {

    private val connectivitySubject: PublishSubject<Boolean> = PublishSubject.create()

    /**
     * An observable that will push out any changes to the network connectivity of the device. This takes into
     * account wifi, cellular, and VPN and will only push out a false if all 3 are down or if there is a
     * slight delay in the handshake from one to the other.
     */
    val isConnected: Observable<Boolean>
        get() = connectivitySubject.subscribeOn(Schedulers.io()).distinctUntilChanged()

    internal fun notify() {
        connectivitySubject.onNext(ConnectivityStateHolder.isConnected)
    }
}