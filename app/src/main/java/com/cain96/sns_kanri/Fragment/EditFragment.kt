package com.cain96.sns_kanri.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cain96.sns_kanri.Data.InternalRecord
import com.cain96.sns_kanri.Data.Record.Record
import com.cain96.sns_kanri.Dialog.DatePick
import com.cain96.sns_kanri.Dialog.TimeSetDialog
import com.cain96.sns_kanri.MainActivity
import com.cain96.sns_kanri.R
import com.cain96.sns_kanri.Utils.getHour
import com.cain96.sns_kanri.Utils.getMinute
import com.cain96.sns_kanri.Utils.showErrorToast
import com.cain96.sns_kanri.Utils.showSuccessToast
import com.cain96.sns_kanri.Utils.toString
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_record.*
import kotlinx.coroutines.experimental.runBlocking

class EditFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private var record: Record? = null

    companion object {
        fun createInstance(mainActivity: MainActivity, record: Record? = null): EditFragment {
            val recordFragment = EditFragment()
            val args = Bundle()
            recordFragment.arguments = args
            recordFragment.mainActivity = mainActivity
            recordFragment.record = record
            return recordFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        record?.let {
            mainActivity.record = InternalRecord(
                it.id,
                it.date,
                it.time.getHour(),
                it.time.getMinute(),
                it.sns
            )
        }
        btn_sns.text = mainActivity.record.sns.name
        mainActivity.record.date.toString("yyyy/MM/dd")?.let {
            btn_date.text = it
        }
        mainActivity.record.timeToText()?.let {
            btn_time.text = it
        }
        btn_sns.setOnClickListener {
            mainActivity.transitionHelper
                .replaceTransition(fragmentManager, SelectFragment.createInstance(mainActivity, false))
        }
        btn_date.setOnClickListener {
            DatePick().show(fragmentManager, "datePicker")
        }
        btn_time.setOnClickListener {
            val timeSetFragment = TimeSetDialog.createInstance(mainActivity.record)
            timeSetFragment.okButtonClickListener = View.OnClickListener {
                mainActivity.record.hour = timeSetFragment.hour()
                mainActivity.record.minutes = timeSetFragment.minutes()
                mainActivity.record.timeToText()?.let {
                    btn_time.text = it
                }
                timeSetFragment.dismiss()
            }
            timeSetFragment.show(fragmentManager, "timeSet")
            timeSetFragment.isCancelable = true
        }
        btn_submit.setOnClickListener {
            val isSubmit = runBlocking {
                return@runBlocking mainActivity.apiHelper.editRecord(mainActivity.record)
            }
            if (isSubmit) {
                showSuccessToast(mainActivity, "Success")
                mainActivity.transitionHelper.replaceTransition(fragmentManager, TabFragment.createInstance(mainActivity))
            } else {
                showErrorToast(mainActivity, "Error")
            }
        }
    }

    override fun setHasOptionsMenu(hasMenu: Boolean) {
        super.setHasOptionsMenu(hasMenu)
        activity?.tool_bar?.apply {
            title = getString(R.string.edit_menu)
            setNavigationIcon(R.mipmap.baseline_clear_white_24)
            setNavigationOnClickListener {
                mainActivity.transitionHelper.replaceTransition(fragmentManager, TabFragment.createInstance(mainActivity))
            }
        }
    }
}
