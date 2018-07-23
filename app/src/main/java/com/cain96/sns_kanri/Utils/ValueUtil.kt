package com.cain96.sns_kanri.Utils

import android.content.res.Resources
import android.graphics.Point
import android.support.v7.app.AppCompatActivity

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun getDisplaySize(activity: AppCompatActivity): Point {
    val display = activity.windowManager.defaultDisplay
    val point = Point()
    display.getSize(point)
    return point
}
