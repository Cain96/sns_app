package com.cain96.sns_kanri.Dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.cain96.sns_kanri.MainActivity
import java.util.Calendar
import java.util.Date

class DatePick : DialogFragment() {
    private var date: Date? = null

    companion object {
        fun createInstance(date: Date? = null): DatePick {
            val datePick = DatePick()
            val args = Bundle()
            datePick.arguments = args
            datePick.date = date
            return datePick
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        date?.let {
            c.time = it
        }
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            activity,
            activity as MainActivity?,
            year,
            month,
            day)
    }
}
