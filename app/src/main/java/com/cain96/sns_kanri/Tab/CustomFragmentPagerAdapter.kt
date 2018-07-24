package com.cain96.sns_kanri.Tab

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.cain96.sns_kanri.Fragment.RecordFragment
import com.cain96.sns_kanri.Fragment.RecordListFragment
import com.cain96.sns_kanri.MainActivity

class CustomFragmentPagerAdapter(
    fm: FragmentManager,
    mainActivity: MainActivity
) : FragmentStatePagerAdapter(fm) {
    private val tabTitles: List<CharSequence> = listOf("記録", "一覧")
    private var mainActivity: MainActivity = mainActivity

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> RecordFragment.createInstance(mainActivity)
            1 -> RecordListFragment.createInstance(mainActivity)
            else -> null
        }
    }

    override fun getCount(): Int {
        return tabTitles.size
    }
}
