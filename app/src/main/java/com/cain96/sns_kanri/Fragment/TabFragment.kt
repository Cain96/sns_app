package com.cain96.sns_kanri.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
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
        R.drawable.baseline_list_alt_24
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
        Log.d("tab", "tab")
        mainActivity.tool_bar.visibility = View.INVISIBLE
        val adapter = CustomFragmentPagerAdapter(fragmentManager as FragmentManager, mainActivity)
        view_pager.offscreenPageLimit = 2
        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)
        setIcons()
    }

    private fun setIcons() {
        icons.forEachIndexed { i, icon_id ->
            tab_layout.getTabAt(i)?.setIcon(icon_id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity.tool_bar.visibility = View.VISIBLE
    }
}
