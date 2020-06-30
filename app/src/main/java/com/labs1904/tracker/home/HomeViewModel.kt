package com.labs1904.tracker.home

import androidx.lifecycle.ViewModel
import com.labs1904.core.defaultIfNull
import com.labs1904.core.format
import com.labs1904.core.livedata.KotlinLiveData
import com.labs1904.tracker.covid.CovidFactory
import com.labs1904.tracker.covid.CovidRepo
import com.labs1904.tracker.covid.NationwideResponse
import com.labs1904.tracker.home.HomeViewModel.Companion.DASH_DASH
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeViewModel(
	private val covidRepository: CovidRepo = CovidFactory.covidRepo,
	private val scheduler: Scheduler = Schedulers.computation()
) : ViewModel() {

	val homeImageURL = KotlinLiveData("https://s7d2.scene7.com/is/image/TWCNews/coronavirus_gen_jpg")

	fun fetchData(): Single<HomeViewData> =
		covidRepository
			.currentValuesNationwide()
			.subscribeOn(scheduler)
			.observeOn(scheduler)
			.map { response ->
				response.firstOrNull()?.let {
					formatResponse(it)
				} ?: HomeViewData()
			}

	private fun formatResponse(nationwideResponse: NationwideResponse): HomeViewData =
		HomeViewData(
			nationwideResponse.totalTestResults?.format().defaultIfNull(DASH_DASH),
			nationwideResponse.positive?.format().defaultIfNull(DASH_DASH),
			nationwideResponse.pending?.format().defaultIfNull(DASH_DASH),
			nationwideResponse.negative?.format().defaultIfNull(DASH_DASH),
			nationwideResponse.recovered?.format().defaultIfNull(DASH_DASH),
			nationwideResponse.death?.format().defaultIfNull(DASH_DASH)
		)

	companion object {
		const val DASH_DASH = "--"
	}
}

data class HomeViewData(
	val totalTestResults: String = DASH_DASH,
	val positiveTestResults: String = DASH_DASH,
	val pendingTestResults: String = DASH_DASH,
	val negativeTestResults: String = DASH_DASH,
	val recovered: String = DASH_DASH,
	val deaths: String = DASH_DASH
)
