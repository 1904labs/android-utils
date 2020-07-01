package com.labs1904.tracker.state

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.labs1904.test_utils.TestActivity
import com.labs1904.test_utils.data.randomInt
import com.labs1904.test_utils.data.randomString
import com.labs1904.test_utils.espresso.assertions.assertSwipeRefreshLayoutIsNotRefreshing
import com.labs1904.test_utils.espresso.assertions.assertSwipeRefreshLayoutIsRefreshing
import com.labs1904.test_utils.extensions.launchFragment
import com.labs1904.tracker.R
import com.labs1904.tracker.covid.CovidFactory
import com.labs1904.tracker.covid.CovidRepo
import com.labs1904.tracker.covid.NationwideResponse
import com.labs1904.tracker.covid.StateResponse
import com.schibsted.spain.barista.interaction.BaristaSwipeRefreshInteractions.refresh
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter
import org.junit.Rule
import org.junit.Test
import java.lang.RuntimeException

class StateDashboardFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    @Test
    fun screen_success() {
        activityRule.launchFragment(StateDashboardFragment(), null)

    }

    @Test
    fun screen_error() {
        activityRule.launchFragment(StateDashboardFragment(), null)

    }

    @Test
    fun screen_loading() {
        activityRule.launchFragment(StateDashboardFragment(), null)

    }

    @Test
    fun pull_to_refresh_stops_on_error() {
        activityRule.launchFragment(StateDashboardFragment(), null)

    }

    @Test
    fun pull_to_refresh_stops_on_success() {
        val response = listOf(generateStateResponse(), generateStateResponse())
        lateinit var singleEmitter: SingleEmitter<List<StateResponse>>

        setUpRepo(Single.just(response))

        activityRule.launchFragment(StateDashboardFragment(), null)

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        setUpRepo(Single.create { emitter -> singleEmitter = emitter })

        refresh(R.id.state_dashboard_swipe_refresh)

        assertSwipeRefreshLayoutIsRefreshing(R.id.state_dashboard_swipe_refresh)

        singleEmitter.onSuccess(response)

        assertSwipeRefreshLayoutIsNotRefreshing(R.id.state_dashboard_swipe_refresh)
    }

    private fun setUpRepo(stateSingle: Single<List<StateResponse>>) {
        CovidFactory.covidRepo = object : CovidRepo {
            override fun currentValuesNationwide(): Single<List<NationwideResponse>> =
                Single.error(RuntimeException())

            override fun currentValuesByState(): Single<List<StateResponse>> = stateSingle
        }
    }

    private fun generateStateResponse(): StateResponse {
        return StateResponse(
            randomString(),
            randomString(),
            randomInt(),
            randomInt(),
            randomString(),
            randomInt(),
            randomInt(),
            randomInt(),
            randomInt(),
            randomInt(),
            randomString(),
            randomInt(),
            randomInt(),
            randomInt(),
            randomInt(),
            randomInt(),
            randomInt(),
            randomInt(),
            randomInt(),
            randomInt(),
            randomInt(),
            randomString(),
            randomInt(),
            randomInt(),
            randomInt()
        )
    }
}