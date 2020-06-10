package com.labs1904.core.livedata

import androidx.lifecycle.LiveData

/**
 * The official <a href="https://developer.android.com/reference/androidx/lifecycle/LiveData">LiveData</a>
 * class is written in Java and provides no Kotlin interoperability. Because of this, it doesn't enforce
 * Kotlin's nullable type system. For example, a LiveData object can be instantiated with a non-null type,
 * but still emit a null value:
 * <pre>
 * val liveData = LiveData<String>()
 *
 * println(liveData.value) //prints null
 * </pre>
 *
 * This can lead to unexpected NullPointerExceptions. To workaround this, we have created a KotlinLiveData class
 * that properly enforces Kotlin's nullable types.
 *
 * Unlike LiveData, KotlinLiveData requires an initial value to be specified.
 */
@Suppress("UNCHECKED_CAST")
open class KotlinLiveData<T>(value: T) : LiveData<T>(value) {
	override fun getValue(): T = super.getValue() as T
}
