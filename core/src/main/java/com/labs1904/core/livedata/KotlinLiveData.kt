package com.labs1904.core.livedata

import androidx.lifecycle.LiveData

@Suppress("UNCHECKED_CAST")
open class KotlinLiveData<T>(value: T) : LiveData<T>(value) {
	override fun getValue(): T = super.getValue() as T
}
