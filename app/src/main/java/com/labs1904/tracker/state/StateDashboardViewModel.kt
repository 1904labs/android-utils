package com.labs1904.tracker.state

import androidx.lifecycle.ViewModel
import com.labs1904.tracker.covid.CovidFactory
import com.labs1904.tracker.covid.CovidRepo
import com.labs1904.tracker.utils.STATE_NAME_MAP
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class StateDashboardViewModel(
    private val covidRepository: CovidRepo = CovidFactory.covidRepo,
    private val scheduler: Scheduler = Schedulers.computation()
) : ViewModel() {

    fun fetchCurrentValuesByState(): Single<List<StateViewData>> =
        covidRepository
            .currentValuesByState()
            .subscribeOn(scheduler)
            .observeOn(scheduler)
            .map { response ->
                response.map { stateData ->
                    StateViewData(
                        STATE_NAME_MAP[stateData.state],
                        stateData.positive,
                        stateData.recovered,
                        stateData.death,
                        stateData.hospitalizedCumulative
                    )
                }
            }
}

data class StateViewData(
    val stateName: String?,
    val numPositiveCases: Int?,
    val numRecovered: Int?,
    val numDeaths: Int?,
    val numHospitalized: Int?
)