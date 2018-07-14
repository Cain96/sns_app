package com.cain96.sns_kanri.Utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm:ss"): Date? {
    if (!pattern.matches(Regex("H|m|s"))) {
    }

    val sdFormat = try {
        SimpleDateFormat(pattern)
    } catch (e: IllegalArgumentException) {
        null
    }
    val date = sdFormat?.let {
        try {
            it.parse(this)
        } catch (e: ParseException) {
            null
        }
    }
    return date
}

fun Date.toString(pattern: String = "yyyy/MM/dd HH:mm:ss"): String? {
    val sdFormat = try {
        SimpleDateFormat(pattern)
    } catch (e: IllegalArgumentException) {
        null
    }
    val date = sdFormat?.let {
        try {
            it.format(this)
        } catch (e: ParseException) {
            null
        }
    }
    return date
}