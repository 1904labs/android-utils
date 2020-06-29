package com.labs1904.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Combines 3 LiveData streams and allows you to perform a certain block of code to the values of the 3 streams
 * to output a LiveData stream of a new data type. The function passed in will be called whenever any of the
 * streams emits a new value.
 *
 * @param liveData1 The first LiveData you want to combine with.
 * @param liveData2 The second LiveData you want to combine with.
 * @param block The block of code you would like to run when any of the 3 streams emit a new value.
 * @return A LiveData stream of a new data type that is the result of the transformation of the 3 original streams within the block function.
 */
fun <T, K, L, R> LiveData<T>.combineWith(
    liveData1: LiveData<K>,
    liveData2: LiveData<L>,
    block: (T?, K?, L?) -> R
): LiveData<R> =
    MediatorLiveData<R>().also { mediatorLiveData ->
        mediatorLiveData.addSource(this) {
            mediatorLiveData.value = block.invoke(this.value, liveData1.value, liveData2.value)
        }
        mediatorLiveData.addSource(liveData1) {
            mediatorLiveData.value = block.invoke(this.value, liveData1.value, liveData2.value)
        }
        mediatorLiveData.addSource(liveData2) {
            mediatorLiveData.value = block.invoke(this.value, liveData1.value, liveData2.value)
        }
    }