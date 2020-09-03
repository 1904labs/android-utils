package com.labs1904.tracker.settings

import android.os.Bundle
import androidx.preference.PreferenceDialogFragmentCompat

class TimePickerSettingsDialog : PreferenceDialogFragmentCompat() {

    override fun onDialogClosed(positiveResult: Boolean) {

    }

    companion object {

        fun newInstance(key: String): TimePickerSettingsDialog =
            TimePickerSettingsDialog().apply {
                arguments = Bundle(1).apply {
                    putString(ARG_KEY, key)
                }
            }
    }
}