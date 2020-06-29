package com.labs1904.tracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.labs1904.tracker.state.StateDashboardFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		main_bottom_nav.setOnNavigationItemSelectedListener {
			when (it.itemId) {
				R.id.item_home -> selectFragment(BottomNavFragments.HOME)
				R.id.item_state_dashboard -> selectFragment(BottomNavFragments.STATE_DASHBOARD)
				else -> false
			}
		}

		main_bottom_nav.selectedItemId = R.id.item_home
	}

	private fun selectFragment(fragmentToSelect: BottomNavFragments): Boolean {
		val transaction = supportFragmentManager.beginTransaction()

		BottomNavFragments.values().forEach {
			if (it == fragmentToSelect) {
				(supportFragmentManager.findFragmentByTag(it.name) ?: createFragment(it, transaction)).let { fragment ->
					transaction.show(fragment)
				}
			} else {
				supportFragmentManager.findFragmentByTag(it.name)?.let { fragment ->
					transaction.hide(fragment)
				}
			}
		}

		transaction.commit()
		return true
	}

	private fun createFragment(fragmentToCreate: BottomNavFragments, transaction: FragmentTransaction) =
		when (fragmentToCreate) {
			BottomNavFragments.HOME -> Fragment()
			BottomNavFragments.STATE_DASHBOARD -> StateDashboardFragment.newInstance()
		}.also {
			transaction.add(R.id.main_content, it, fragmentToCreate.name)
		}
}

enum class BottomNavFragments {
	HOME,
	STATE_DASHBOARD
}
