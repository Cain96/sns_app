package com.cain96.sns_kanri.Tab

import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.PagerAdapter
import com.cain96.sns_kanri.Fragment.RecordFragment
import com.cain96.sns_kanri.Fragment.RecordListFragment
import com.cain96.sns_kanri.Fragment.ReportFragment
import com.cain96.sns_kanri.Fragment.TabFragment
import com.cain96.sns_kanri.MainActivity

class CustomFragmentPagerAdapter(
    fm: FragmentManager,
    mainActivity: MainActivity,
    tabFragment: TabFragment,
    pagerId: Int
) : FragmentStatePagerAdapter(fm) {
    private val tabTitles: List<CharSequence> = listOf("記録", "一覧", "統計情報")
    private val fragments = mutableListOf(
        RecordFragment.createInstance(mainActivity),
        RecordListFragment.createInstance(mainActivity),
        ReportFragment.createInstance(mainActivity)
    )
    private val fm: FragmentManager = fm
    private var ft: FragmentTransaction? = null
    private val pagerId: Int = pagerId
    private val tabFragment: TabFragment = tabFragment

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

    override fun getItem(position: Int): Fragment? {
        return fragments[position]
    }

    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
    }

    override fun notifyDataSetChanged() {
        ft?.let {
            it.commitAllowingStateLoss()
            ft = null
        }
        super.notifyDataSetChanged()
        tabFragment.setIcons()
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun replace(position: Int, fragment: Fragment) {
        val tag = makeFragmentName(pagerId, position)
        if (ft != null) ft = fm.beginTransaction()
        ft?.replace(pagerId, fragment, tag)
        fragments[position] = fragment
    }

    private fun makeFragmentName(viewId: Int, index: Int): String {
        return "android:switcher:$viewId:$index"
    }
}
