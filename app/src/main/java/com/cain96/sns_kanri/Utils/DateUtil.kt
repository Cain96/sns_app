package com.cain96.sns_kanri.Utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm:ss"): Date? {
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

class DateAdapter {

    @ToJson
    fun dateToJson(date: Date): String {
        return date.toString()
    }

    @FromJson
    fun dateFromJson(json: String): Date? {
        json.toDate("yyyy-MM-dd")?.let {
            return it
        }
        json.toDate("HH:mm:ss")?.let {
            return it
        }
        json.toDate("yyyy-MM-dd HH:mm")?.let {
            return it
        }
        return json.toDate()
    }
}
