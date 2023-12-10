package com.elmirov.vkcompose.data.converter

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    private const val IN_MILLIS = 1000

    fun Long.convertTimestampToDate(): String {
        val date = Date(this * IN_MILLIS)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}