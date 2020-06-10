package com.labs1904.core.livedata

class KotlinMutableLiveData<T>(value: T) : KotlinLiveData<T>(value) {
	public override fun setValue(value: T) = super.setValue(value)
	public override fun postValue(value: T) = super.postValue(value)
}
