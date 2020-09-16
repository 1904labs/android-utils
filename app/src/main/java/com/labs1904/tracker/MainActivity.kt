package com.labs1904.tracker

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.labs1904.connectivity_notifier.ConnectivityNotifier
import com.labs1904.connectivity_notifier.ConnectivityStateHolder
import com.labs1904.tracker.home.HomeFragment
import com.labs1904.tracker.settings.SettingsFragment
import com.labs1904.tracker.state.StateDashboardFragment
import com.labs1904.ui.extensions.dpToPx
import com.labs1904.ui.extensions.getContentView
import com.labs1904.ui.extensions.getDefaultSharedPrefs
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val networkLostSnackbar: Snackbar? by lazy { createNoConnectionSnackbar() }
    private val backOnlineToast: Toast? by lazy { createBackOnlineToast() }
    private val sharedPrefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (getString(R.string.enable_network_monitoring_key) == key) {
            checkForDisconnection()
            setUpNetworkMonitoring()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_bottom_nav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_home -> selectFragment(BottomNavFragments.HOME)
                R.id.item_state_dashboard -> selectFragment(BottomNavFragments.STATE_DASHBOARD)
                R.id.item_settings -> selectFragment(BottomNavFragments.SETTINGS)
                else -> false
            }
        }

        main_bottom_nav.selectedItemId = R.id.item_home

        getDefaultSharedPrefs().registerOnSharedPreferenceChangeListener(sharedPrefsListener)
        setUpNetworkMonitoring()
    }

    override fun onResume() {
        super.onResume()
        checkForDisconnection()
    }

    override fun onDestroy() {
        disposables.clear()
        getDefaultSharedPrefs().unregisterOnSharedPreferenceChangeListener(sharedPrefsListener)
        super.onDestroy()
    }

    private fun checkForDisconnection() {
        if (isNetworkMonitoringEnabled() && !ConnectivityStateHolder.isConnected) {
            onDisconnected()
        }
    }

    private fun isNetworkMonitoringEnabled(): Boolean =
        getDefaultSharedPrefs().getBoolean(getString(R.string.enable_network_monitoring_key), true)

    private fun setUpNetworkMonitoring() {
        if (isNetworkMonitoringEnabled()) {
            disposables.add(
                ConnectivityNotifier
                    .isConnected
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ isConnected ->
                        if (isConnected) {
                            onConnected()
                        } else {
                            onDisconnected()
                        }
                    }, {
                        Timber.e(
                            it,
                            "MainActivity.ConnectivityNotifier.isConnected error"
                        )
                    })
            )
        } else {
            disposables.clear()
            networkLostSnackbar?.dismiss()
            backOnlineToast?.cancel()
        }
    }

    private fun onConnected() {
        networkLostSnackbar?.dismiss()
        backOnlineToast?.show()
    }

    private fun onDisconnected() {
        backOnlineToast?.cancel()

        if (networkLostSnackbar?.isShown == false) {
            networkLostSnackbar?.show()
        }
    }

    private fun createNoConnectionSnackbar(): Snackbar? = getContentView()?.let { contentView ->
        Snackbar
            .make(
                contentView,
                getString(R.string.network_connection_lost),
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction(getString(R.string.dismiss)) { networkLostSnackbar?.dismiss() }
            .setActionTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            .apply {
                view.background =
                    ContextCompat.getDrawable(this@MainActivity, R.drawable.rounded_rectangle)
                view.translationY = -dpToPx(SNACKBAR_TRANSLATION_Y_DP)
            }
    }

    private fun createBackOnlineToast(): Toast? =
        Toast(this).apply {
            duration = Toast.LENGTH_LONG
            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, dpToPx(TOAST_TRANSLATION_Y_DP).toInt())
            view = layoutInflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_layout))
                .apply {
                    findViewById<AppCompatTextView>(R.id.toast_message).setTextColor(ContextCompat.getColor(this@MainActivity, R.color.toastTextColor))
                    findViewById<AppCompatTextView>(R.id.toast_message).text = getString(R.string.network_connection_restored)
                }
        }

    private fun selectFragment(fragmentToSelect: BottomNavFragments): Boolean {
        checkForDisconnection()

        val transaction = supportFragmentManager.beginTransaction()

        BottomNavFragments.values().forEach {
            if (it == fragmentToSelect) {
                (supportFragmentManager.findFragmentByTag(it.name) ?: createFragment(
                    it,
                    transaction
                )).let { fragment ->
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

    private fun createFragment(
        fragmentToCreate: BottomNavFragments,
        transaction: FragmentTransaction
    ) =
        when (fragmentToCreate) {
            BottomNavFragments.HOME -> HomeFragment.newInstance()
            BottomNavFragments.STATE_DASHBOARD -> StateDashboardFragment.newInstance()
            BottomNavFragments.SETTINGS -> SettingsFragment.newInstance()
        }.also {
            transaction.add(R.id.main_content, it, fragmentToCreate.name)
        }

    companion object {
        private const val SNACKBAR_TRANSLATION_Y_DP: Float = 60f
        private const val TOAST_TRANSLATION_Y_DP: Float = 75f

        fun launch(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }
}

enum class BottomNavFragments {
    HOME,
    STATE_DASHBOARD,
    SETTINGS
}
