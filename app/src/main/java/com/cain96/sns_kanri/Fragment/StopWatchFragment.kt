package com.cain96.sns_kanri.Fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cain96.sns_kanri.MainActivity
import com.cain96.sns_kanri.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_stop_watch.*

class StopWatchFragment : Fragment() {
    private lateinit var mainActivity: MainActivity

    private val handler = Handler()
    private var timeValue: Int = 0
    private var isMove: Boolean = false
    private lateinit var runnable: Runnable

    companion object {
        fun createInstance(mainActivity: MainActivity): StopWatchFragment {
            val stopWatchFragment = StopWatchFragment()
            val args = Bundle()
            stopWatchFragment.mainActivity = mainActivity
            stopWatchFragment.arguments = args
            return stopWatchFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_stop_watch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        timeToText()?.let {
            timeText.text = it
        }

        runnable = object : Runnable {
            override fun run() {
                timeValue++
                timeToText(timeValue)?.let {
                    timeText.text = it
                }
                handler.postDelayed(this, 1000)
            }
        }

        if (!isMove) handler.post(runnable)
        isMove = true

        stop.setOnClickListener {
            if (isMove) handler.removeCallbacks(runnable)
            isMove = false
            mainActivity.setTime(timeValue)
            mainActivity.transitionHelper
                .replaceTransition(fragmentManager, TabFragment.createInstance(mainActivity))
        }
    }

    override fun setHasOptionsMenu(hasMenu: Boolean) {
        super.setHasOptionsMenu(hasMenu)
        activity?.let {
            it.tool_bar.title = getString(R.string.stop_watch_menu)
            it.tool_bar.setNavigationIcon(R.mipmap.baseline_clear_white_24)
            it.tool_bar.setNavigationOnClickListener {
                handler.removeCallbacks(runnable)
                mainActivity.transitionHelper
                    .replaceTransition(fragmentManager, TabFragment.createInstance(mainActivity))
            }
        }
    }

    private fun timeToText(time: Int = 0): String? {
        return if (time < 0) {
            null
        } else if (time == 0) {
            "00:00:00"
        } else {
            val h = time / 3600
            val m = time % 3600 / 60
            val s = time % 60
            "%1$02d:%2$02d:%3$02d".format(h, m, s)
        }
    }
}
