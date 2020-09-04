package com.labs1904.tracker.settings

import android.content.Context
import android.util.AttributeSet
import androidx.preference.DialogPreference
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimePreference(context: Context?, attrs: AttributeSet?) : DialogPreference(context, attrs) {

    override fun getDialogTitle(): CharSequence = ""

    override fun getSummary(): CharSequence = createSummary()

    fun setTime(time: String) {
        persistString(time)
        summary = createSummary()
    }

    fun to12HourString(hour: Int, minute: Int): String =
        LocalTime.of(hour, minute, 0, 0)
            .format(DateTimeFormatter.ofPattern(TWELVE_HOUR_TIME_FORMAT))

    fun getHour(): Int = getPersistedLocalTime().hour

    fun getMinute(): Int = getPersistedLocalTime().minute

    private fun createSummary(): String =
        getPersistedLocalTime().format(DateTimeFormatter.ofPattern(TWELVE_HOUR_TIME_FORMAT))

    private fun getPersistedLocalTime(): LocalTime = LocalTime.parse(
        getPersistedString(DEFAULT_VALUE),
        DateTimeFormatter.ofPattern(TWELVE_HOUR_TIME_FORMAT)
    )

    companion object {
        private const val DEFAULT_VALUE = "12:00 AM"
        private const val TWELVE_HOUR_TIME_FORMAT = "h:mm a"
    }
}