package com.cain96.sns_kanri.Fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cain96.sns_kanri.Data.Statistics.Rate
import com.cain96.sns_kanri.Data.Statistics.Statistic
import com.cain96.sns_kanri.MainActivity
import com.cain96.sns_kanri.R
import com.cain96.sns_kanri.Utils.add
import com.cain96.sns_kanri.Utils.rangeList
import com.cain96.sns_kanri.Utils.toString
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import kotlinx.android.synthetic.main.fragment_report.*
import kotlinx.coroutines.experimental.runBlocking
import java.util.Date

class ReportFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private var startDate: Date = Date()

    companion object {
        fun createInstance(mainActivity: MainActivity): ReportFragment {
            val reportFragment = ReportFragment()
            val args = Bundle()
            reportFragment.arguments = args
            reportFragment.mainActivity = mainActivity
            return reportFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTime()

        setChart(startDate)
        btn_bar_navigate_next.setOnClickListener {
            startDate = startDate.add(7)
            setChart(startDate)
        }
        btn_bar_navigate_before.setOnClickListener {
            startDate = startDate.add(-7)
            setChart(startDate)
        }
    }

    private fun setTime() {
        val time = runBlocking {
            mainActivity.apiHelper.requestTime()
        }
        time?.let {
            today_hour.text = "%.1f".format(it.day)
            this_week_hour.text = "%.1f".format(it.week)
            this_month_hour.text = "%.1f".format(it.month)
        }
    }

    private fun setChart(startDate: Date) {
        val dateList = startDate.rangeList(-6)
        val statistics = runBlocking {
            mainActivity.apiHelper.requestStatistics(dateList.first(), dateList.last())
        }
        createBarChart(dateList, statistics?.statistic)
        createPieChart(statistics?.rate)
    }

    private fun createBarChart(dateList: List<Date>, statistic: List<Statistic>?) {
        bar_chart.axisRight.isEnabled = false
        bar_chart.axisLeft.apply {
            isEnabled = true
            textSize = 15f
            axisMinimum = 0f
        }

        bar_chart.apply {
            setDrawGridBackground(true)
            setDrawBarShadow(true)
            description.isEnabled = false
            isDoubleTapToZoomEnabled = false
            isClickable = false
            legend.isEnabled = false
            isAutoScaleMinMaxEnabled = true
            setScaleEnabled(true)
            animateY(2000, Easing.EasingOption.EaseInQuart)
        }

        val xValues = dateList.map {
            it.toString("MM/dd")
        }

        date_text_view.text = "%s - %s".format(
            dateList.first().toString("MM/dd"),
            dateList.last().toString("MM/dd")
        )

        bar_chart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(xValues)
            textSize = 14f
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(true)
            setDrawGridLines(false)
            setDrawAxisLine(true)
            labelRotationAngle = 45f
        }

        bar_chart.data = BarData(getBarData(statistic)).apply {
            setValueTextSize(15f)
        }
    }

    private fun getBarData(list: List<Statistic>?): ArrayList<IBarDataSet> {
        var values = ArrayList<BarEntry>()
        list?.forEachIndexed { index, statistic ->
            values.add(BarEntry(index.toFloat(), statistic.sns.toFloatArray()))
        }
        val barDataSet = BarDataSet(values, "").apply {
            valueFormatter = IValueFormatter { value, _, _, _ -> "%.1f h".format(value) }
            colors = getBarColors()
        }

        val barDataSets: ArrayList<IBarDataSet> = ArrayList()
        barDataSets.add(barDataSet)

        return barDataSets
    }

    private fun createPieChart(rate: List<Rate>?) {
        val pieData = PieData(getPieDataSet(rate)).apply {
            setValueFormatter(PercentFormatter())
            setValueTextSize(22f)
            setValueTextColor(Color.WHITE)
        }
        pie_chart.apply {
            data = pieData
            description.isEnabled = false
            isClickable = false
            legend.isEnabled = false
            holeRadius = 30f
            transparentCircleRadius = 50f
            animateXY(
                1000,
                1000,
                Easing.EasingOption.EaseInBack,
                Easing.EasingOption.EaseInOutQuart
            )
        }
    }

    @SuppressLint("Range")
    private fun getPieDataSet(rate: List<Rate>?): PieDataSet {
        var entry = ArrayList<PieEntry>()
        var colorsList = ArrayList<Int>()
        rate?.forEach {
            entry.add(PieEntry(it.num, it.sns.name))
            colorsList.add(Color.parseColor(it.sns.color))
        }
        return PieDataSet(entry, "").apply {
            colors = colorsList
            setDrawValues(true)
        }
    }

    @SuppressLint("Range")
    private fun getBarColors(): ArrayList<Int> {
        var colors = ArrayList<Int>()
        mainActivity.snsList?.forEach {
            colors.add(Color.parseColor(it.color))
        }
        return colors
    }
}
