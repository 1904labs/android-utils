package com.labs1904.tracker.settings

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.labs1904.tracker.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        context?.let {
            setDivider(ContextCompat.getDrawable(it, R.drawable.settings_separator))
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

    companion object {
        fun newInstance() = SettingsFragment()
    }
}