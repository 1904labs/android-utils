package com.labs1904.tracker.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TimePicker
import androidx.preference.PreferenceDialogFragmentCompat
import com.labs1904.core.safeLet

class TimePickerSettingsDialog : PreferenceDialogFragmentCompat() {

    private var timePicker: TimePicker? = null

    override fun onCreateDialogView(context: Context?): View {
        return TimePicker(context).apply {
            layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
            timePicker = this
        }
    }

    override fun onBindDialogView(view: View?) {
        super.onBindDialogView(view)
        timePicker?.apply {
            getTimePreference()?.let {
                setHourOnPicker(it.getHour())
                setMinuteOnPicker(it.getMinute())
            }
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            getTimePreference()?.let {
                safeLet(getHour(), getMinute()) { hour, minute ->
                    val time = it.to12HourString(hour, minute)
                    if (it.callChangeListener(time)) {
                        it.setTime(time)
                    }
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getMinute(): Int? {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            timePicker?.minute
        } else {
            timePicker?.currentMinute
        }
    }

    @Suppress("DEPRECATION")
    private fun getHour(): Int? {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            timePicker?.hour
        } else {
            timePicker?.currentHour
        }
    }

    @Suppress("DEPRECATION")
    private fun setHourOnPicker(hour: Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            timePicker?.hour = hour
        } else {
            timePicker?.currentHour = hour
        }
    }

    @Suppress("DEPRECATION")
    private fun setMinuteOnPicker(minute: Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            timePicker?.minute = minute
        } else {
            timePicker?.currentMinute = minute
        }
    }

    private fun getTimePreference(): TimePreference? = preference as? TimePreference

    companion object {

        fun newInstance(key: String): TimePickerSettingsDialog =
            TimePickerSettingsDialog().apply {
                arguments = Bundle(1).apply {
                    putString(ARG_KEY, key)
                }
            }
    }
}