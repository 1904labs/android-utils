package com.labs1904.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Uses [block] to transform the values of two LiveData streams into a new LiveData stream of type [R].
 * The [block] function will be called whenever either of the two input streams emits a value.
 *
 * @param liveData The LiveData stream that will be combined with [this].
 * @param block The transforming block of code to be run when either of the 2 streams emits a value.
 * @return A LiveData stream of type [R] that emits the result of [block].
 */
fun <T, K, R> LiveData<T>.combineWith(
	liveData: LiveData<K>,
	block: (T?, K?) -> R
): LiveData<R> =
	MediatorLiveData<R>().also { mediatorLiveData ->
		mediatorLiveData.addSource(this) {
			mediatorLiveData.value = block.invoke(this.value, liveData.value)
		}
		mediatorLiveData.addSource(liveData) {
			mediatorLiveData.value = block.invoke(this.value, liveData.value)
		}
	}

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

/**
 * Uses [block] to transform any number of LiveData streams (of type [T]) into a new LiveData stream of type [R].
 * The [block] function will be called whenever any of the input streams emits a value.
 *
 * @param liveDataArray The LiveData streams that will be combined with [this].
 * @param block The transforming block of code to be run when any of the streams emits a value.
 * @return A LiveData stream of type [R] that emits the result of [block].
 */
fun <T, R> LiveData<T>.combineWith(
	vararg liveDataArray: LiveData<T>,
	block: (List<T?>) -> R
): LiveData<R> =
	MediatorLiveData<R>().also { mediatorLiveData ->
		mediatorLiveData.addSource(this) {
			mediatorLiveData.value =
				block.invoke(listOf(this.value) + liveDataArray.map { it.value })
		}

		liveDataArray.forEach { liveData ->
			mediatorLiveData.addSource(liveData) {
				mediatorLiveData.value =
					block.invoke(listOf(this.value) + liveDataArray.map { it.value })
			}
		}
	}
