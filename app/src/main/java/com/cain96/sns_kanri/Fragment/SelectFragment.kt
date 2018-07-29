package com.cain96.sns_kanri.Fragment

import android.annotation.SuppressLint
import android.graphics.Color
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
        val snsList = mainActivity.snsList
        val point = getDisplaySize(mainActivity)
        val oneThird = point.x / 3 - 10.toPx()
        lateinit var horizontal: LinearLayout
        val lastIndex = snsList?.let {
            return@let it.lastIndex
        }
        snsList?.reversed()?.forEachIndexed { i, sns ->
            if (i % 3 == 0) horizontal = createHorizontalLayout(oneThird)
            val layoutParams = createLayout(oneThird)
            val button = createButton(sns, oneThird)
            horizontal.addView(button, layoutParams)
            if (i % 3 == 2 || i == lastIndex) select_container.addView(horizontal)
        }
    }

    override fun setHasOptionsMenu(hasMenu: Boolean) {
        super.setHasOptionsMenu(hasMenu)
        activity?.tool_bar?.apply {
            title = getString(R.string.select_menu)
            setNavigationIcon(R.mipmap.baseline_clear_white_24)
            setNavigationOnClickListener {
                mainActivity.transitionHelper
                    .replaceTransition(fragmentManager, TabFragment.createInstance(mainActivity))
            }
        }
    }

    private fun createHorizontalLayout(height: Int): LinearLayout {
        return LinearLayout(activity).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                height
            )
        }
    }

    @SuppressLint("Range")
    private fun createButton(sns: Sns, buttonSize: Int): Button {
        return Button(activity).apply {
            text = sns.name
            width = buttonSize
            height = buttonSize
            setBackgroundColor(Color.parseColor(sns.color))
            gravity = Gravity.CENTER
            textSize = 23f
            setTextColor(resources.getColor(R.color.white))
            setAllCaps(false)
            setOnClickListener {
                mainActivity.record.sns = sns.copy()
                mainActivity.transitionHelper.replaceTransition(
                    fragmentManager, TabFragment.createInstance(mainActivity)
                )
            }
        }
    }

    private fun createLayout(buttonSize: Int): LinearLayout.LayoutParams {
        var layoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize)
        val margin = 5.toPx()
        layoutParams.setMargins(margin, margin, margin, margin)
        return layoutParams
    }
}
