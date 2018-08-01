package com.cain96.sns_kanri.Dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.cain96.sns_kanri.Fragment.RecordListFragment
import com.cain96.sns_kanri.Fragment.ReportFragment
import com.cain96.sns_kanri.MainActivity
import com.cain96.sns_kanri.R
import com.cain96.sns_kanri.Utils.showErrorToast
import com.cain96.sns_kanri.Utils.showSuccessToast
import kotlinx.coroutines.experimental.runBlocking

class DeleteDialog : DialogFragment() {
    lateinit var mainActivity: MainActivity
    var recordId: Int = 0

    companion object {
        fun createInstance(mainActivity: MainActivity, id: Int): DeleteDialog {
            val deleteDialogFragment = DeleteDialog()
            val args = Bundle()
            deleteDialogFragment.mainActivity = mainActivity
            deleteDialogFragment.recordId = id
            deleteDialogFragment.arguments = args
            return deleteDialogFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity).apply {
            setTitle(R.string.delete_title)
            setMessage(R.string.delete_message)
            setPositiveButton(R.string.delete_ok, DialogInterface.OnClickListener { _, _ ->
                val isDelete = runBlocking {
                    mainActivity.apiHelper.deleteRecord(recordId)
                }
                if (isDelete) {
                    showSuccessToast(mainActivity, "Success")
                    mainActivity.adapter?.replace(
                        1,
                        RecordListFragment.createInstance(mainActivity)
                    )
                    mainActivity.adapter?.replace(
                        2,
                        ReportFragment.createInstance(mainActivity)
                    )
                    mainActivity.adapter?.notifyDataSetChanged()
                } else {
                    showErrorToast(mainActivity, "Error")
                }
            })
            setNegativeButton(
                R.string.delete_cancel,
                DialogInterface.OnClickListener { _, _ -> }
            )
        }
        return builder.create()
    }
}
