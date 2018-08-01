package com.cain96.sns_kanri.Utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
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

fun Date.rangeList(range: Int): List<Date> {
    val dateList: ArrayList<Date> = ArrayList()
    val calendar = Calendar.getInstance()
    for (i in 0 downTo range) {
        calendar.time = this
        calendar.add(Calendar.DAY_OF_MONTH, i)
        dateList.add(calendar.time)
    }
    return dateList.reversed()
}

fun Date.add(num: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.add(Calendar.DAY_OF_MONTH, num)
    return calendar.time
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

fun Date.getHour(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.HOUR_OF_DAY)
}

fun Date.getMinute(): Int {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return calendar.get(Calendar.MINUTE)
}
