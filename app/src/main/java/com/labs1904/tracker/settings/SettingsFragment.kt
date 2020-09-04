package com.labs1904.tracker.settings

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.preference.*
import com.labs1904.core.safeLet
import com.labs1904.push_notifications.PushNotificationHelperProvider
import com.labs1904.tracker.R
import com.labs1904.tracker.utils.PushNotificationHandler
import com.labs1904.ui.extensions.getDefaultSharedPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            setDivider(ContextCompat.getDrawable(it, R.drawable.settings_separator))
        }

        findPreference<MultiSelectListPreference>(getString(R.string.notification_days_key))?.apply {
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                (newValue as? Set<String>)?.let { days ->
                    scheduleNotifications(areNotificationsEnabled(), days, getTime())
                    setDayPreferenceSummary(days)
                    true
                } ?: false
            }
            setDayPreferenceSummary(values)
        }

        findPreference<DialogPreference>(getString(R.string.notification_time_key))?.apply {
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                (newValue as? String)?.let { time ->
                    scheduleNotifications(areNotificationsEnabled(), getDays(), time)
                    true
                } ?: false
            }
        }

        findPreference<SwitchPreferenceCompat>(getString(R.string.enable_notifications_key))?.apply {
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                (newValue as? Boolean)?.let { enabled ->
                    scheduleNotifications(enabled, getDays(), getTime())
                    true
                } ?: false
            }
        }
    }

    override fun onDisplayPreferenceDialog(preference: Preference?) {
        if (preference is TimePreference) {
            TimePickerSettingsDialog.newInstance(preference.key).apply {
                setTargetFragment(this@SettingsFragment, TIME_PICKER_REQUEST_CODE)
            }.show(parentFragmentManager, TimePickerSettingsDialog::class.java.simpleName)
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

    private fun MultiSelectListPreference.setDayPreferenceSummary(values: Set<String>) {
        summary = when (values.size) {
            NO_DAYS_OF_WEEK -> getString(R.string.not_set)
            ALL_DAYS_OF_WEEK -> getString(R.string.every_day)
            else -> values.sortedBy { findIndexOfValue(it) }
                .joinToString(DAY_SEPARATOR) { it.substring(0..2) }
        }
    }

    private fun scheduleNotifications(isEnabled: Boolean, days: Set<String>?, time: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            getPushNotificationHelper()?.let { helper ->
                if (isEnabled) {
                    safeLet(time, days) { t, d ->
                        helper.cancelScheduledNotifications()
                        helper.scheduleWithTimeAndDays(d.toList(), t)
                    }
                } else {
                    helper.cancelScheduledNotifications()
                }
            }
        }
    }

    private fun areNotificationsEnabled(): Boolean = activity?.getDefaultSharedPrefs()?.getBoolean(getString(R.string.enable_notifications_key), false) ?: false

    private fun getDays(): Set<String>? = activity?.getDefaultSharedPrefs()?.getStringSet(getString(R.string.notification_days_key), null)

    private fun getTime(): String? = activity?.getDefaultSharedPrefs()?.getString(getString(R.string.notification_time_key), null)

    private fun getPushNotificationHelper(): PushNotificationHandler? =
        (activity?.application as? PushNotificationHelperProvider)?.get() as? PushNotificationHandler

    companion object {
        private const val DAY_SEPARATOR = ", "
        private const val TIME_PICKER_REQUEST_CODE = 0
        private const val NO_DAYS_OF_WEEK = 0
        private const val ALL_DAYS_OF_WEEK = 7

        fun newInstance() = SettingsFragment()
    }
}