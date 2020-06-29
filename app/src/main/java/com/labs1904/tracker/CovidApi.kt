package com.labs1904.tracker

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface CovidApi {

	@GET("us/current.json")
	fun currentValuesNationwide(): Single<List<NationwideResponse>>

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
	val hash: String?
)
