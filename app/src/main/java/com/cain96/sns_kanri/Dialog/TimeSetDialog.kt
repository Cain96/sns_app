package com.cain96.sns_kanri.Dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.NumberPicker
import com.cain96.sns_kanri.Data.InternalRecord
import com.cain96.sns_kanri.R
import kotlinx.android.synthetic.main.dialog_time_set.*
import java.io.Serializable

class TimeSetDialog : DialogFragment() {
    lateinit var okButtonClickListener: View.OnClickListener

    lateinit var internalRecord: InternalRecord

    companion object {
        fun createInstance(record: Serializable): TimeSetDialog {
            val timeSetFragment = TimeSetDialog()
            val args = Bundle()
            args.putSerializable("RECORD", record)
            timeSetFragment.arguments = args
            return timeSetFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val bundle = arguments
        internalRecord = bundle?.getSerializable("RECORD") as InternalRecord
        return inflater.inflate(R.layout.dialog_time_set, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNumberPicker(num_picker_hour, 23, 0, internalRecord.hour)
        setNumberPicker(num_picker_minutes1, 5, 0, internalRecord.minutes / 10)
        setNumberPicker(num_picker_minutes2, 9, 0, internalRecord.minutes % 10)
        btn_time_set_ok.setOnClickListener(okButtonClickListener)
        btn_time_set_cancel.setOnClickListener {
            this.dismiss()
        }
    }

    private fun setNumberPicker(numberPicker: NumberPicker, max: Int, min: Int, init: Int) {
        numberPicker.wrapSelectorWheel = true
        numberPicker.maxValue = max
        numberPicker.minValue = min
        numberPicker.value = init
    }

    fun hour(): Int {
        return num_picker_hour.value
    }

    fun minutes(): Int {
        return num_picker_minutes1.value * 10 + num_picker_minutes2.value
    }
}
