package com.cain96.sns_kanri.Helper

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.cain96.sns_kanri.R

class TransitionHelper {
    fun replaceTransition(fragmentManager: FragmentManager?, fragmentInstance: Fragment) {
        fragmentManager?.let {
            val transition = it.beginTransaction()
            transition.replace(R.id.mainContainer, fragmentInstance)
            transition.addToBackStack(null)
            transition.commit()
        }
    }

    fun replaceNoBackStackTransition(
        fragmentManager: FragmentManager?,
        fragmentInstance: Fragment
    ) {
        fragmentManager?.let {
            val transition = it.beginTransaction()
            transition.replace(R.id.mainContainer, fragmentInstance)
            transition.commit()
        }
    }
}
