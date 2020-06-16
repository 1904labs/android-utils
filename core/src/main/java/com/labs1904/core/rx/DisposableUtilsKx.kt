package com.labs1904.core.rx

import io.reactivex.rxjava3.disposables.Disposable

/**
 * Checks whether this Disposable instance is null or has been disposed.
 *
 * @return true if this Disposable is null or has been disposed, false otherwise.
 */
fun Disposable?.isNullOrDisposed(): Boolean =
	this == null || this.isDisposed
