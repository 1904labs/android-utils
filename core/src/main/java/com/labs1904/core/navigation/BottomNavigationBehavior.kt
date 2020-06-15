package com.labs1904.core.navigation

import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.labs1904.core.livedata.KotlinLiveData
import com.labs1904.core.livedata.KotlinMutableLiveData

class BottomNavigationBehavior(
	private val bottomNavigationView: BottomNavigationView,
	private val bottomNavigationItems: List<BottomNavigationItem>,
	private val fragmentManager: FragmentManager,
	@IdRes private val fragmentContainerId: Int
) : BottomNavigationView.OnNavigationItemSelectedListener,
	BottomNavigationView.OnNavigationItemReselectedListener,
	FragmentManager.OnBackStackChangedListener {

	private val _currentNavController: KotlinMutableLiveData<NavController?> = KotlinMutableLiveData(null)
	private var selectedItemTag: String

	val currentNavController: KotlinLiveData<NavController?> = _currentNavController

	init {
		bottomNavigationItems.forEach {
			val fragmentTag = getNavHostFragmentTag(it.itemId)
			val navHostFragment = obtainNavHostFragment(fragmentTag, it.navGraphId)

			if (bottomNavigationView.selectedItemId == it.itemId) {
				_currentNavController.value = navHostFragment.navController

				fragmentManager
					.beginTransaction()
					.attach(navHostFragment)
					.setPrimaryNavigationFragment(navHostFragment)
					.commitNow()
			}
		}

		selectedItemTag = getNavHostFragmentTag(bottomNavigationView.selectedItemId)

		bottomNavigationView.setOnNavigationItemSelectedListener(this)
		bottomNavigationView.setOnNavigationItemReselectedListener(this)

		fragmentManager.addOnBackStackChangedListener(this)
	}

	override fun onNavigationItemSelected(item: MenuItem): Boolean =
		if (fragmentManager.isStateSaved) {
			false
		} else {
			val selectedBottomNavigationItem = bottomNavigationItems.first { it.itemId == item.itemId }
			val newlySelectedItemTag = getNavHostFragmentTag(item.itemId)
			if (selectedItemTag != newlySelectedItemTag) {
				val selectedFragment = obtainNavHostFragment(newlySelectedItemTag, selectedBottomNavigationItem.navGraphId)

				val transaction = fragmentManager.beginTransaction()

				if (selectedFragment.isAdded) {
					transaction.show(selectedFragment)
				} else {
					transaction.attach(selectedFragment)
				}

				hideOtherFragments(transaction, newlySelectedItemTag)

				transaction
					.setPrimaryNavigationFragment(selectedFragment)
					.setReorderingAllowed(true)
					.commit()

				selectedItemTag = newlySelectedItemTag
				_currentNavController.value = selectedFragment.navController

				true
			} else {
				false
			}
		}

	override fun onNavigationItemReselected(item: MenuItem) {
		bottomNavigationItems.first { it.itemId == item.itemId }.let {
			val navController = obtainNavHostFragment(getNavHostFragmentTag(it.itemId), it.navGraphId).navController

			navController.popBackStack(navController.graph.startDestination, false)
		}
	}

	override fun onBackStackChanged() {
		currentNavController.value?.takeIf { it.currentDestination == null }?.let {
			it.navigate(it.graph.id)
		}
	}

	private fun hideOtherFragments(transaction: FragmentTransaction, newlySelectedItemTag: String) {
		bottomNavigationItems.forEach {
			val fragmentTag = getNavHostFragmentTag(it.itemId)
			if (fragmentTag != newlySelectedItemTag) {
				val fragment = obtainNavHostFragment(fragmentTag, it.navGraphId)
				if (fragment.isAdded) transaction.hide(fragment)
			}
		}
	}

	private fun obtainNavHostFragment(fragmentTag: String, @NavigationRes navGraphId: Int): NavHostFragment =
		fragmentManager.findFragmentByTag(fragmentTag) as? NavHostFragment ?: NavHostFragment.create(navGraphId).also {
				fragmentManager
					.beginTransaction()
					.add(fragmentContainerId, it, fragmentTag)
					.detach(it)
					.commitNow()
			}

	private fun getNavHostFragmentTag(@IdRes itemId: Int): String =
		"navHostFragment-$itemId"
}

data class BottomNavigationItem(
	@IdRes val itemId: Int,
	@NavigationRes val navGraphId: Int
)
