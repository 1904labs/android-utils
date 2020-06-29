package com.labs1904.tracker.home

import androidx.lifecycle.ViewModel
import com.labs1904.core.livedata.KotlinLiveData
import com.labs1904.core.livedata.KotlinMutableLiveData
import com.labs1904.tracker.covid.CovidFactory
import com.labs1904.tracker.covid.CovidRepo
import com.labs1904.tracker.covid.NationwideResponse
import com.labs1904.tracker.home.HomeViewModel.Companion.DASH_DASH
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel(
	private val covidRepository: CovidRepo = CovidFactory.covidRepo,
	private val scheduler: Scheduler = Schedulers.computation()
) : ViewModel() {

	private val _nationwideData = KotlinMutableLiveData<NationwideViewData?>(null)
	val nationwideData: KotlinLiveData<NationwideViewData?> = _nationwideData

	fun fetchData(): Completable =
		covidRepository
			.currentValuesNationwide()
			.subscribeOn(scheduler)
			.observeOn(scheduler)
			.map { response ->
				response.firstOrNull()?.let {
					formatResponse(it)
				} ?: NationwideViewData()
			}
			.doOnSuccess { _nationwideData.postValue(it) }
			.doOnError { _nationwideData.postValue(NationwideViewData()) }
			.ignoreElement()

	private fun formatResponse(nationwideResponse: NationwideResponse): NationwideViewData =
		NationwideViewData()

	companion object {
		const val DASH_DASH = "--"
	}
}

data class NationwideViewData(
	val totalTestResults: String = DASH_DASH
)
