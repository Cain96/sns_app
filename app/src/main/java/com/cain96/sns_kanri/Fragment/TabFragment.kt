package com.cain96.sns_kanri.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cain96.sns_kanri.MainActivity
import com.cain96.sns_kanri.R
import com.cain96.sns_kanri.Tab.CustomFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_tab.*

class TabFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private val icons: List<Int> = listOf(
        R.drawable.baseline_create_24,
        R.drawable.baseline_list_alt_24,
        R.drawable.baseline_pie_chart_24
    )

    companion object {
        fun createInstance(mainActivity: MainActivity): TabFragment {
            val tabFragment = TabFragment()
            val args = Bundle()
            tabFragment.mainActivity = mainActivity
            tabFragment.arguments = args
            return tabFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity.tool_bar.visibility = View.INVISIBLE
        view_pager.offscreenPageLimit = 3
        mainActivity.adapter = CustomFragmentPagerAdapter(
            fragmentManager as FragmentManager,
            mainActivity,
            this,
            R.id.tab_layout
        )
        view_pager.adapter = mainActivity.adapter
        tab_layout.setupWithViewPager(view_pager)
        setIcons()
    }

    fun setIcons() {
        icons.forEachIndexed { i, icon_id ->
            tab_layout.getTabAt(i)?.setIcon(icon_id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.tool_bar.visibility = View.VISIBLE
    }
}
