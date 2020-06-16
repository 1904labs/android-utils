package com.labs1904.core.livedata

/**
 * The official <a href="https://developer.android.com/reference/androidx/lifecycle/MutableLiveData">MutableLiveData</a>
 * class is written in Java and provides no Kotlin interoperability. Because of this, it doesn't enforce
 * Kotlin's nullable type system. For example, a MutableLiveData object can be instantiated with a non-null type,
 * but still emit a null value. A null value can also be passed into it:
 * <pre>
 * val liveData = MutableLiveData<String>()
 *
 * println(liveData.value) //prints null
 *
 * liveData.value = null   //valid
 * </pre>
 *
 * This can lead to unexpected NullPointerExceptions. To workaround this, we have created a KotlinMutableLiveData class
 * that properly enforces Kotlin's nullable types.
 *
 * Unlike MutableLiveData, KotlinMutableLiveData requires an initial value to be specified.
 */
class KotlinMutableLiveData<T>(value: T) : KotlinLiveData<T>(value) {
	public override fun setValue(value: T) = super.setValue(value)
	public override fun postValue(value: T) = super.postValue(value)
}
