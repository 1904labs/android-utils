package com.labs1904.tracker.state

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.labs1904.test_utils.TestActivity
import com.labs1904.test_utils.data.randomInt
import com.labs1904.test_utils.data.randomString
import com.labs1904.test_utils.espresso.assertions.assertSwipeRefreshLayoutIsNotRefreshing
import com.labs1904.test_utils.espresso.assertions.assertSwipeRefreshLayoutIsRefreshing
import com.labs1904.test_utils.espresso.assertions.assertViewEffectivelyGone
import com.labs1904.test_utils.espresso.assertions.assertViewEffectivelyVisible
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

// TODO: Finish and get these tests to work
class StateDashboardFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(TestActivity::class.java, false, false)

    @Test
    fun screen_success() {
        activityRule.launchFragment(StateDashboardFragment(), null)

    }

    @Test
    fun screen_error() {
        setUpRepo(Single.error(RuntimeException()))

        activityRule.launchFragment(StateDashboardFragment(), null)

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

    }

    @Test
    fun screen_loading() {
        lateinit var singleEmitter: SingleEmitter<List<StateResponse>>

        setUpRepo(Single.create { emitter -> singleEmitter = emitter })

        activityRule.launchFragment(StateDashboardFragment(), null)

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        assertViewEffectivelyVisible(R.id.state_dashboard_progress_bar)
        assertViewEffectivelyGone(R.id.state_recycler_view)

        singleEmitter.onError(RuntimeException())

        assertViewEffectivelyGone(R.id.state_dashboard_progress_bar)
    }

    @Test
    fun pull_to_refresh_stops_on_error() {
        lateinit var singleEmitter: SingleEmitter<List<StateResponse>>

        setUpRepo(Single.error(RuntimeException()))

        activityRule.launchFragment(StateDashboardFragment(), null)

        InstrumentationRegistry.getInstrumentation().waitForIdleSync()

        setUpRepo(Single.create { emitter -> singleEmitter = emitter })

        refresh(R.id.state_dashboard_swipe_refresh)

        assertSwipeRefreshLayoutIsRefreshing(R.id.state_dashboard_swipe_refresh)

        singleEmitter.onError(RuntimeException())

        assertSwipeRefreshLayoutIsNotRefreshing(R.id.state_dashboard_swipe_refresh)
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