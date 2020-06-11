package com.labs1904.core.rx

import io.reactivex.rxjava3.disposables.Disposable

/**
 * Return true if this Disposable is null or has been disposed.
 *
 * @return true if this Disposable is null or has been disposed.
 */
fun Disposable?.isNullOrDisposed(): Boolean =
	this == null || this.isDisposed
