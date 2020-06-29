package com.labs1904.tracker.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.labs1904.tracker.R
import com.labs1904.ui.views.BaseFragment
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import timber.log.Timber

class HomeFragment : BaseFragment() {

	private val viewModel: HomeViewModel by viewModels()
	private val compositeDisposable = CompositeDisposable()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = inflater.inflate(R.layout.fragment_home, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		viewModel.nationwideData.observe(viewLifecycleOwner, Observer {
			Timber.d("HELLO $it")
		})

		compositeDisposable.add(
			viewModel
				.fetchData()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe({
					Timber.d("viewModel.fetchData() success")
				}, {
					Timber.e(it, "viewModel.fetchData() error")
				})
		)
	}

	override fun onDestroyView() {
		compositeDisposable.clear()
		super.onDestroyView()
	}

	companion object {
		fun newInstance(): HomeFragment = HomeFragment()
	}
}
