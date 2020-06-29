package com.labs1904.tracker.covid

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

interface CovidRepo {
	fun currentValuesNationwide(): Single<List<NationwideResponse>>
	fun currentValuesByState(): Single<List<StateResponse>>
}

class CovidRepository(
	private val covidApi: CovidApi,
	private val scheduler: Scheduler = Schedulers.io()
) : CovidRepo {

	override fun currentValuesNationwide(): Single<List<NationwideResponse>> =
		covidApi
			.currentValuesNationwide()
			.subscribeOn(scheduler)

	override fun currentValuesByState(): Single<List<StateResponse>> =
		covidApi
			.currentValuesByState()
			.subscribeOn(scheduler)
}
