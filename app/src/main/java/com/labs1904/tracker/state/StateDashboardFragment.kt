package com.labs1904.tracker.state

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.labs1904.tracker.R
import com.labs1904.ui.extensions.addItemSeparator
import com.labs1904.ui.extensions.gone
import com.labs1904.ui.extensions.showDialogWithDismiss
import com.labs1904.ui.extensions.visible
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_state_dashboard.*

class StateDashboardFragment : Fragment() {

    private lateinit var viewModel: StateDashboardViewModel
    private val adapter: StateAdapter by lazy { StateAdapter() }
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_state_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(StateDashboardViewModel::class.java)
        state_recycler_view.adapter = adapter
        state_recycler_view.addItemSeparator(separatorDrawable = R.drawable.separator_drawable)

        state_dashboard_swipe_refresh.setOnRefreshListener {
            fetchData()
        }

        fetchData()
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }

    private fun fetchData() {
        compositeDisposable.add(
            viewModel
                .fetchCurrentValuesByState()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { setViewState(StateDashboardViewState.LOADING) }
                .doFinally { state_dashboard_swipe_refresh.isRefreshing = false }
                .subscribe({ setViewState(StateDashboardViewState.SUCCESS, it) },
                    { setViewState(StateDashboardViewState.ERROR) }
                )
        )
    }

    private fun setViewState(
        viewState: StateDashboardViewState,
        viewData: List<StateViewData>? = null
    ) {
        state_dashboard_progress_bar.gone()
        state_recycler_view.gone()

        when (viewState) {
            StateDashboardViewState.SUCCESS -> {
                adapter.submitList(viewData)
                state_recycler_view.visible()
            }
            StateDashboardViewState.LOADING -> {
                state_dashboard_progress_bar.visible()
            }
            StateDashboardViewState.ERROR -> {
                showDialogWithDismiss(
                    getString(R.string.network_error),
                    getString(R.string.failed_to_fetch_data)
                )
            }
        }
    }

    companion object {
        fun newInstance() = StateDashboardFragment()
    }
}

enum class StateDashboardViewState {
    LOADING,
    SUCCESS,
    ERROR
}