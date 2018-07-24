package com.cain96.sns_kanri.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.cain96.sns_kanri.Data.Sns.Sns
import com.cain96.sns_kanri.MainActivity
import com.cain96.sns_kanri.R
import com.cain96.sns_kanri.Utils.getDisplaySize
import com.cain96.sns_kanri.Utils.toPx
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_select.*
import kotlinx.coroutines.experimental.runBlocking

class SelectFragment : Fragment() {
    lateinit var mainActivity: MainActivity

    companion object {
        fun createInstance(mainActivity: MainActivity): SelectFragment {
            val selectFragment = SelectFragment()
            val args = Bundle()
            selectFragment.mainActivity = mainActivity
            selectFragment.arguments = args
            return selectFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_select, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        val layout = select_container
        val point = getDisplaySize(mainActivity)
        val oneThird = point.x / 3 - 10.toPx()
        val snsList = runBlocking {
            mainActivity.apiHelper.requestSns()
        }
        lateinit var horizontal: LinearLayout
        val lastIndex = snsList?.let {
            return@let it.lastIndex
        }
        snsList?.reversed()?.forEachIndexed { i, sns ->
            if (i % 3 == 0) horizontal = createHorizontalLayout(oneThird)
            val layoutParams = createLayout(oneThird)
            val button = createButton(sns, oneThird)
            horizontal.addView(button, layoutParams)
            if (i % 3 == 2 || i == lastIndex) layout.addView(horizontal)
        }
    }

    override fun setHasOptionsMenu(hasMenu: Boolean) {
        super.setHasOptionsMenu(hasMenu)
        activity?.let {
            it.tool_bar.title = getString(R.string.select_menu)
            it.tool_bar.setNavigationIcon(R.mipmap.baseline_clear_white_24)
            it.tool_bar.setNavigationOnClickListener {
                mainActivity.transitionHelper
                    .replaceTransition(fragmentManager, TabFragment.createInstance(mainActivity))
            }
        }
    }

    private fun createHorizontalLayout(height: Int): LinearLayout {
        var horizontal = LinearLayout(activity)
        horizontal.orientation = LinearLayout.HORIZONTAL
        horizontal.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            height
        )
        return horizontal
    }

    private fun createButton(sns: Sns, buttonSize: Int): Button {
        var button = Button(activity)
        button.text = sns.name
        button.width = buttonSize
        button.height = buttonSize
        button.setBackgroundResource(R.drawable.buttonbg)
        button.gravity = Gravity.CENTER or Gravity.BOTTOM
        button.setOnClickListener {
            mainActivity.record.sns = sns.copy()
            mainActivity.transitionHelper.replaceTransition(
                fragmentManager, TabFragment.createInstance(mainActivity)
            )
        }
        return button
    }

    private fun createLayout(buttonSize: Int): LinearLayout.LayoutParams {
        var layoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize)
        val margin = 5.toPx()
        layoutParams.setMargins(margin, margin, margin, margin)
        return layoutParams
    }
}
