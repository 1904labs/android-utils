package com.labs1904.tracker.settings

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.labs1904.tracker.R

@Suppress("UNCHECKED_CAST")
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

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
                setTargetFragment(this@SettingsFragment, 0)
            }.show(parentFragmentManager, TimePickerSettingsDialog::class.java.simpleName)
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

    private fun MultiSelectListPreference.setDayPreferenceSummary(values: Set<String>) {
        summary = when (values.size) {
            0 -> getString(R.string.not_set)
            7 -> getString(R.string.every_day)
            else -> values.sortedBy { findIndexOfValue(it) }.joinToString(DAY_SEPARATOR) { it.substring(0..2) }
        }
    }

    companion object {
        private const val DAY_SEPARATOR = ", "

        fun newInstance() = SettingsFragment()
    }
}