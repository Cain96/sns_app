package com.cain96.sns_kanri.Recycler

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.cain96.sns_kanri.R

class RecordRecyclerViewHolder(
    view: View,
    listener: ItemClickListener?
) : RecyclerView.ViewHolder(view), View.OnClickListener {
    interface ItemClickListener {
        fun onItemClick(view: View, position: Int)
    }

    val listener = listener
    val textSns: TextView = view.findViewById(R.id.text_sns)
    val itemDate: TextView = view.findViewById(R.id.item_date)
    val itemTime: TextView = view.findViewById(R.id.item_time)
    val buttonEdit: ImageButton = view.findViewById(R.id.edit)
    val buttonDelete: ImageButton = view.findViewById(R.id.delete)

    init {
        buttonEdit.setOnClickListener(this)
        buttonDelete.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        listener?.let {
            it.onItemClick(v, layoutPosition)
        }
    }
}
