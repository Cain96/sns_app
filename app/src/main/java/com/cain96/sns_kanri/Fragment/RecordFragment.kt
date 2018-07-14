package com.cain96.sns_kanri.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cain96.sns_kanri.Dialog.DatePick
import com.cain96.sns_kanri.Dialog.TimeSetDialog
import com.cain96.sns_kanri.MainActivity
import com.cain96.sns_kanri.R
import com.cain96.sns_kanri.Utils.toString
import kotlinx.android.synthetic.main.fragment_record.*

class RecordFragment : Fragment() {
    private lateinit var mainActivity: MainActivity

    companion object {
        fun createInstance(mainActivity: MainActivity): RecordFragment {
            val recordFragment = RecordFragment()
            val args = Bundle()
            recordFragment.arguments = args
            recordFragment.mainActivity = mainActivity
            return recordFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.record.date.toString("yyyy/MM/dd")?.let {
            btn_date.text = it
        }

        timeToText(mainActivity.record.hour, mainActivity.record.minutes)?.let {
            btn_time.text = it
        }
        btn_date.setOnClickListener {
            DatePick().show(fragmentManager, "datePicker")
        }
        btn_time.setOnClickListener {
            val timeSetFragment = TimeSetDialog.createInstance(mainActivity.record)
            timeSetFragment.okButtonClickListener = View.OnClickListener {
                mainActivity.record.hour = timeSetFragment.hour()
                mainActivity.record.minutes = timeSetFragment.minutes()
                timeToText(mainActivity.record.hour, mainActivity.record.minutes)?.let {
                    btn_time.text = it
                }
                timeSetFragment.dismiss()
            }
            timeSetFragment.show(fragmentManager, "timeSet")
            timeSetFragment.isCancelable = true
        }
        timer.setOnClickListener {
            mainActivity.transitionHelper
                .replaceTransition(fragmentManager, StopWatchFragment.createInstance(mainActivity))
        }
    }

    private fun timeToText(h: Int = 0, m: Int = 0): String? {
        return if (h < 0 || m < 0) {
            null
        } else {
            "%1$02d : %2$02d".format(h, m)
        }
    }
}
