package com.elmirov.vkcompose.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    private const val IN_MILLIS = 1000

    fun Long.convertTimestampToDate(): String {
        val date = Date(this * IN_MILLIS)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }

    fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> =
        merge(this, another)
}