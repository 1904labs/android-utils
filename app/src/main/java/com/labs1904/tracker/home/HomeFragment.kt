package com.labs1904.tracker.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.labs1904.tracker.R
import com.labs1904.tracker.databinding.FragmentHomeBinding
import com.labs1904.ui.extensions.gone
import com.labs1904.ui.extensions.showDialogWithDismiss
import com.labs1904.ui.extensions.visible
import com.labs1904.ui.views.BaseFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

class HomeFragment : BaseFragment() {

	private val viewModel: HomeViewModel by viewModels()
	private val compositeDisposable = CompositeDisposable()
	private lateinit var binding: FragmentHomeBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = DataBindingUtil.inflate<FragmentHomeBinding>(inflater, R.layout.fragment_home, container, false).let {
		binding = it

		it.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.viewModel = viewModel

		home_swipe_layout.setOnRefreshListener {
			fetchData()
		}

		setViewState(HomeViewState.LOADING)
		fetchData()
	}

	override fun onDestroyView() {
		compositeDisposable.clear()
		super.onDestroyView()
	}

	private fun fetchData() {
		compositeDisposable.add(
			viewModel
				.fetchData()
				.observeOn(AndroidSchedulers.mainThread())
				.doFinally { home_swipe_layout.isRefreshing = false }
				.subscribe({
					Timber.d("viewModel.fetchData() success")

					setViewState(HomeViewState.SUCCESS, it)
				}, {
					Timber.e(it, "viewModel.fetchData() error")

					setViewState(HomeViewState.ERROR)
				})
		)
	}

	private fun setViewState(homeViewState: HomeViewState, homeViewData: HomeViewData? = null) {
		home_content.gone()
		home_progress_bar.gone()

		when (homeViewState) {
			HomeViewState.LOADING -> home_progress_bar.visible()
			HomeViewState.SUCCESS -> {
				home_content.visible()

				total_tests_card.value = homeViewData?.totalTestResults
				total_positive_card.value = homeViewData?.positiveTestResults
				total_pending_card.value = homeViewData?.pendingTestResults
				total_negative_card.value = homeViewData?.negativeTestResults

				total_recovered_card.value = homeViewData?.recovered
				total_deaths_card.value = homeViewData?.deaths
			}
			HomeViewState.ERROR -> {
				showDialogWithDismiss(
					getString(R.string.network_error),
					getString(R.string.failed_to_fetch_data)
				)
			}
		}
	}

	companion object {
		fun newInstance(): HomeFragment = HomeFragment()
	}
}

enum class HomeViewState {
	LOADING,
	SUCCESS,
	ERROR
}
