package com.rfcreations.animenotepad.utils

import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

//format the date from milliseconds into readable form
fun convertCreationDate(dateInMilli: Long, context: Context): String {
    lateinit var date: String
    val isSystemIn24HourFormat = DateFormat.is24HourFormat(context)
    date = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
        if (isSystemIn24HourFormat) ({
            SimpleDateFormat("HH:mm", Locale.getDefault())
        }).toString() else ({
            SimpleDateFormat("h:mm a", Locale.getDefault())
        }).toString()
    } else {
        val dateToFormat = Instant.ofEpochMilli(dateInMilli).atZone(ZoneId.systemDefault())
        DateTimeFormatter.ofPattern("dd MMM").format(dateToFormat)
    }
    return date
}
