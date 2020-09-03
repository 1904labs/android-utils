package com.labs1904.tracker.settings

import android.content.Context
import android.util.AttributeSet
import androidx.preference.DialogPreference
import com.labs1904.tracker.R

class TimePreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {

    init {
        dialogTitle = null
        dialogLayoutResource = R.layout.time_picker
    }
}