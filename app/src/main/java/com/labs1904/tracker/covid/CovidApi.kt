package com.labs1904.tracker.covid

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface CovidApi {

	@GET("us/current.json")
	fun currentValuesNationwide(): Single<List<NationwideResponse>>

	@GET("states/current.json")
	fun currentValuesByState(): Single<List<StateResponse>>

}

data class NationwideResponse(
	val date: String?,
	val states: Int?,
	val positive: Int?,
	val negative: Int?,
	val pending: Int?,
	val hospitalizedCurrently: Int?,
	val hospitalizedCumulative: Int?,
	val inIcuCurrently: Int?,
	val inIcuCumulative: Int?,
	val onVentilatorCurrently: Int?,
	val onVentilatorCumulative: Int?,
	val recovered: Int?,
	val dateChecked: String?,
	val death: Int?,
	val hash: String?,
	val totalTestResults: Int?
)

data class StateResponse(
	val dataQualityGrade: String?,
	val date: String?,
	val death: Int?,
	val deathIncrease: Int?,
	val fips: String?,
	val hospitalizedCumulative: Int?,
	val hospitalizedCurrently: Int?,
	val hospitalizedIncrease: Int?,
	val inIcuCumulative: Int?,
	val inIcuCurrently: Int?,
	val lastUpdateEt: String?,
	val negative: Int?,
	val negativeTestsViral: Int?,
	val onVentilatorCumulative: Int?,
	val onVentilatorCurrently: Int?,
	val pending: Int?,
	val positive: Int?,
	val positiveCasesViral: Int?,
	val positiveIncrease: Int?,
	val positiveTestsViral: Int?,
	val recovered: Int?,
	val state: String?,
	val totalTestResults: Int?,
	val totalTestResultsIncrease: Int?,
	val totalTestsViral: Int?
)
