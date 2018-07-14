package com.cain96.sns_kanri

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cain96.sns_kanri.Fragment.RecordFragment
import com.cain96.sns_kanri.Helper.TransitionHelper

class MainActivity : AppCompatActivity() {
    val transitionHelper = TransitionHelper()
    var hour = 0
    var minutes = 0

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
        hour = time / 3600
        minutes = time % 3600 / 60
    }
}
