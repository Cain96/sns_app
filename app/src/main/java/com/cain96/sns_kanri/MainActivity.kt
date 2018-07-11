package com.cain96.sns_kanri

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cain96.sns_kanri.Fragment.StopWatchFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.mainContainer, StopWatchFragment.createInstance())
            transaction.commit()
        }
    }
}
