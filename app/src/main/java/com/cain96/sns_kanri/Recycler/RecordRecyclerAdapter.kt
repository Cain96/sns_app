package com.cain96.sns_kanri.Recycler

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.cain96.sns_kanri.Data.Record.Record
import com.cain96.sns_kanri.R
import com.cain96.sns_kanri.Utils.toString

class RecordRecyclerAdapter(
    private val context: Context,
    private val itemClickListener: RecordRecyclerViewHolder.ItemClickListener,
    private val itemList: List<Record>
) : RecyclerView.Adapter<RecordRecyclerViewHolder>() {
    private var mRecyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null
    }

    @SuppressLint("Range", "ResourceType")
    override fun onBindViewHolder(holder: RecordRecyclerViewHolder, position: Int) {
        holder.let {
            if (itemList.isNotEmpty()) {
                val record: Record = itemList[position]
                it.textSns.text = record.sns.name
                it.itemDate.text = record.date.toString("yyyy/MM/dd")
                it.itemTime.text = record.time.toString("HH : mm")
                it.textSns.setBackgroundColor(Color.parseColor(record.sns.color))
            } else {
                it.textSns.text = context.getString(R.string.record_none)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordRecyclerViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val mView = layoutInflater.inflate(R.layout.record_item_view, parent, false)
        mView.setOnClickListener { view ->
            mRecyclerView?.let {
                itemClickListener.onItemClick(view, it.getChildAdapterPosition(view))
            }
        }
        return RecordRecyclerViewHolder(mView)
    }
}
