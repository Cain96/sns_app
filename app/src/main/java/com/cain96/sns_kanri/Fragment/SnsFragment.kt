package com.cain96.sns_kanri.Fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cain96.sns_kanri.Data.Sns.Sns
import com.cain96.sns_kanri.MainActivity
import com.cain96.sns_kanri.R
import com.cain96.sns_kanri.Utils.showErrorToast
import com.cain96.sns_kanri.Utils.showSuccessToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sns.*
import kotlinx.coroutines.experimental.runBlocking

class SnsFragment : Fragment() {
    lateinit var mainActivity: MainActivity
    private var color: String? = null
    private var isCreate: Boolean = false

    companion object {
        fun createInstance(mainActivity: MainActivity, isCreate: Boolean): SnsFragment {
            val snsFragment = SnsFragment()
            val args = Bundle()
            snsFragment.mainActivity = mainActivity
            snsFragment.isCreate = isCreate
            snsFragment.arguments = args
            return snsFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_sns, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        color_picker.addOnColorSelectedListener {
            color = "#%s".format(Integer.toHexString(it))
        }
        btn_submit.setOnClickListener {
            val isSubmit = runBlocking {
                return@runBlocking mainActivity.apiHelper.createSns(
                    Sns(name = name_sns.text.toString(), color = color)
                )
            }
            if (isSubmit) {
                showSuccessToast(mainActivity, "Success")
                mainActivity.snsList = runBlocking {
                    return@runBlocking mainActivity.apiHelper.requestSns()
                }
                mainActivity.transitionHelper.replaceNoBackStackTransition(
                    fragmentManager,
                    SelectFragment.createInstance(mainActivity, isCreate)
                )
            } else {
                showErrorToast(mainActivity, "Error")
            }
        }
    }

    override fun setHasOptionsMenu(hasMenu: Boolean) {
        super.setHasOptionsMenu(hasMenu)
        activity?.tool_bar?.apply {
            title = getString(R.string.sns_menu)
            setNavigationIcon(R.mipmap.baseline_clear_white_24)
            setNavigationOnClickListener {
                fragmentManager?.popBackStack()
            }
        }
    }
}
