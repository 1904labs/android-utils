package com.labs1904.tracker.settings

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.labs1904.tracker.R

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
            onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValues ->
                (newValues as? Set<String>)?.let {
                    setDayPreferenceSummary(it)
                    true
                } ?: false
            }
            setDayPreferenceSummary(values)
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
            else -> values.sortedBy { findIndexOfValue(it) }.joinToString(DAY_SEPARATOR) { it.substring(0..2) }
        }
    }

    companion object {
        private const val DAY_SEPARATOR = ", "
        private const val TIME_PICKER_REQUEST_CODE = 0
        private const val NO_DAYS_OF_WEEK = 0
        private const val ALL_DAYS_OF_WEEK = 7

        fun newInstance() = SettingsFragment()
    }
}