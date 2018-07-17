package com.cain96.sns_kanri

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.DatePicker
import com.cain96.sns_kanri.Data.Record
import com.cain96.sns_kanri.Fragment.RecordFragment
import com.cain96.sns_kanri.Helper.TransitionHelper
import com.cain96.sns_kanri.Utils.toDate
import kotlinx.android.synthetic.main.fragment_record.*

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    val transitionHelper = TransitionHelper()
    var record = InternalRecord()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.mainContainer, RecordFragment.createInstance(this))
            transaction.commit()
        }
    }

    fun setTime(time: Int) {
        record.hour = time / 3600
        record.minutes = time % 3600 / 60
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        "%d/%d/%d 00:00:00".format(year, month, dayOfMonth).toDate()?.let {
            record.date = it
        }
        btn_date.text = "%d/%d/%d".format(year, month, dayOfMonth)
    }
}
