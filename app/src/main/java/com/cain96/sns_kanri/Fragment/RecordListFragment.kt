package com.cain96.sns_kanri.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cain96.sns_kanri.Data.Record.Record
import com.cain96.sns_kanri.Dialog.DeleteDialog
import com.cain96.sns_kanri.MainActivity
import com.cain96.sns_kanri.R
import com.cain96.sns_kanri.Recycler.RecordRecyclerAdapter
import com.cain96.sns_kanri.Recycler.RecordRecyclerViewHolder
import kotlinx.coroutines.experimental.runBlocking

class RecordListFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    var recordList: List<Record> = listOf()

    companion object {
        fun createInstance(mainActivity: MainActivity): RecordListFragment {
            val recordListFragment = RecordListFragment()
            val args = Bundle()
            recordListFragment.mainActivity = mainActivity
            recordListFragment.arguments = args
            return recordListFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_record_list, container, false)

        if (view is RecyclerView) {
            val context = view.context
            val recyclerView = view
            recyclerView.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false)
            val records = runBlocking {
                mainActivity.apiHelper.requestRecords()
            }
            recordList = records?.results ?: listOf()
            recyclerView.adapter = RecordRecyclerAdapter(
                mainActivity, itemClickListener, recordList)
        }
        return view
    }

    private val itemClickListener: RecordRecyclerViewHolder.ItemClickListener =
        object : RecordRecyclerViewHolder.ItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val record = recordList[position]
                when (view.id) {
                    R.id.edit -> {
                        mainActivity.transitionHelper.replaceTransition(
                            fragmentManager,
                            EditFragment.createInstance(mainActivity, record)
                        )
                    }
                    R.id.delete -> {
                        val dialog = DeleteDialog.createInstance(mainActivity, record.id)
                        dialog.show(fragmentManager, "delete")
                    }
                }
            }
        }
}
