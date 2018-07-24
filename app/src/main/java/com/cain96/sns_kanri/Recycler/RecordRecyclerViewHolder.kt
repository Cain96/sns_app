package com.cain96.sns_kanri.Recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.cain96.sns_kanri.R

class RecordRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    val itemSns: TextView = view.findViewById(R.id.item_sns)
    val itemDate: TextView = view.findViewById(R.id.item_date)
    val itemTime: TextView = view.findViewById(R.id.item_time)
}
