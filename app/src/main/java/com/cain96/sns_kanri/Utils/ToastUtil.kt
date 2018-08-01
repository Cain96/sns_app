package com.cain96.sns_kanri.Utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.cain96.sns_kanri.R

fun showSuccessToast(context: Context, message: String, time: Int = Toast.LENGTH_LONG) {
    val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflate.inflate(R.layout.success_toast, null)

    val textView = view.findViewById(R.id.message) as TextView
    textView.text = message

    Toast(context).run {
        this.view = view
        duration = time
        setGravity(Gravity.TOP, 0, 250)
        show()
    }
}

fun showErrorToast(context: Context, message: String, time: Int = Toast.LENGTH_LONG) {
    val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflate.inflate(R.layout.fail_toast, null)

    val textView = view.findViewById(R.id.message) as TextView
    textView.text = message

    Toast(context).run {
        this.view = view
        duration = time
        setGravity(Gravity.TOP, 0, 250)
        show()
    }
}
