package com.cain96.sns_kanri.Fragment

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import com.cain96.sns_kanri.Data.Sns.Sns
import com.cain96.sns_kanri.MainActivity
import com.cain96.sns_kanri.R
import com.cain96.sns_kanri.Utils.getDisplaySize
import com.cain96.sns_kanri.Utils.showErrorToast
import com.cain96.sns_kanri.Utils.showSuccessToast
import com.cain96.sns_kanri.Utils.toPx
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_select.*
import kotlinx.coroutines.experimental.runBlocking

class SelectFragment : Fragment() {
    lateinit var mainActivity: MainActivity

    var isCreate: Boolean = false
    private var isFabOpen: Boolean = false
    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var rotateForward: Animation
    private lateinit var rotateBackward: Animation

    companion object {
        fun createInstance(mainActivity: MainActivity, isCreate: Boolean): SelectFragment {
            val selectFragment = SelectFragment()
            val args = Bundle()
            selectFragment.mainActivity = mainActivity
            selectFragment.isCreate = isCreate
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
        snsList?.forEachIndexed { i, sns ->
            if (i % 3 == 0) horizontal = createHorizontalLayout(oneThird)
            val layoutParams = createLayout(oneThird)
            val button = createButton(sns, oneThird)
            horizontal.addView(button, layoutParams)
            if (i % 3 == 2 || i == lastIndex) select_container.addView(horizontal)
        }
        fabOpen = AnimationUtils.loadAnimation(mainActivity, R.anim.fab_open)
        fabClose = AnimationUtils.loadAnimation(mainActivity, R.anim.fab_close)
        rotateForward = AnimationUtils.loadAnimation(mainActivity, R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(mainActivity, R.anim.rotate_backward)
        fab.setOnClickListener {
            animateFab()
        }
        fab1.setOnClickListener {
            mainActivity.transitionHelper.replaceTransition(
                fragmentManager,
                SnsFragment.createInstance(mainActivity, isCreate)
            )
        }
        fab2.setOnClickListener {
            val items: Array<String>? = snsList?.map { sns: Sns -> sns.name }?.toTypedArray()
            val defaultItem = 0
            var checkedItem = 0
            val builder = AlertDialog.Builder(mainActivity).apply {
                setTitle(R.string.sns_delete)
                setSingleChoiceItems(items, defaultItem, DialogInterface.OnClickListener { _, which ->
                    checkedItem = which
                })
                setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                    if (checkedItem >= 0 && snsList != null) {
                        val sns: Sns = snsList[checkedItem]
                        sns.enabled = false
                        val isSubmit = runBlocking {
                            return@runBlocking mainActivity.apiHelper.editSns(sns)
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
                })
                setNegativeButton("キャンセル", null)
            }
            builder.show()
        }
    }

    override fun setHasOptionsMenu(hasMenu: Boolean) {
        super.setHasOptionsMenu(hasMenu)
        activity?.tool_bar?.apply {
            title = getString(R.string.select_menu)
            setNavigationIcon(R.mipmap.baseline_clear_white_24)
            setNavigationOnClickListener {
                if (isCreate) {
                    mainActivity.transitionHelper.replaceTransition(
                        fragmentManager, TabFragment.createInstance(mainActivity)
                    )
                } else {
                    mainActivity.transitionHelper.replaceTransition(
                        fragmentManager, EditFragment.createInstance(mainActivity)
                    )
                }
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
            setTextColor(ContextCompat.getColor(context, R.color.white))
            setAllCaps(false)
            setOnClickListener {
                mainActivity.record.sns = sns.copy()
                if (isCreate) {
                    mainActivity.transitionHelper.replaceTransition(
                        fragmentManager, TabFragment.createInstance(mainActivity)
                    )
                } else {
                    mainActivity.transitionHelper.replaceTransition(
                        fragmentManager, EditFragment.createInstance(mainActivity)
                    )
                }
            }
        }
    }

    private fun createLayout(buttonSize: Int): LinearLayout.LayoutParams {
        var layoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize)
        val margin = 5.toPx()
        layoutParams.setMargins(margin, margin, margin, margin)
        return layoutParams
    }

    private fun animateFab() {
        if (isFabOpen) {
            fab.startAnimation(rotateBackward)
            fab1.startAnimation(fabClose)
            fab2.startAnimation(fabClose)
            fab1_label.startAnimation(fabClose)
            fab2_label.startAnimation(fabClose)
            fab1.isClickable = false
            fab2.isClickable = false
            isFabOpen = false
        } else {
            fab.startAnimation(rotateForward)
            fab1.startAnimation(fabOpen)
            fab2.startAnimation(fabOpen)
            fab1_label.startAnimation(fabOpen)
            fab2_label.startAnimation(fabOpen)
            fab1.isClickable = true
            fab2.isClickable = true
            isFabOpen = true
        }
    }
}
