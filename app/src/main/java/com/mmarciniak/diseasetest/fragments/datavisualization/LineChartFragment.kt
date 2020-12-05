package com.mmarciniak.diseasetest.fragments.datavisualization

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.mmarciniak.diseasetest.R
import com.mmarciniak.diseasetest.data.UserScore
import kotlinx.android.synthetic.main.line_chart_dialog.view.*
import kotlinx.android.synthetic.main.pie_chart_dialog.view.cancel_button
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit


class LineChartFragment : DialogFragment() {
    private lateinit var lineData: ArrayList<UserScore>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lineData = arguments?.getParcelableArrayList<UserScore>(scoresKey) ?: throw IllegalStateException("No args provided")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.line_chart_dialog, container, false)


        populateLineChart(lineData,rootView)

        rootView.cancel_button.setOnClickListener{
            dismiss()
        }

        return rootView
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }


    private fun populateLineChart(scores: List<UserScore>, rootView: View) {
//        val date = LocalDate.parse(string, DateTimeFormatter.ISO_DATE)
        val datesMap = mutableMapOf<String, Int>()
        for (score in scores) {
            val value = datesMap[score.date] ?: 0
            datesMap[score.date] = value + 1
        }

        val lineEntries = mutableListOf<Entry>()
        val format = DateTimeFormatter.ofPattern("dd-M-yyyy")
        for ((key, value) in datesMap) {
            val date = LocalDate.parse(key, format)
            val dateLong = date.toEpochDay()
            lineEntries.add(Entry(dateLong.toFloat(), value.toFloat()))
        }

        val lineDataSet = LineDataSet(lineEntries, "LineChart Data")
        lineDataSet.colors = ColorTemplate.COLORFUL_COLORS.asList()
        lineDataSet.valueTextColor = Color.BLACK
        lineDataSet.valueTextSize = 18f

        val lineData = LineData(lineDataSet)

        rootView.line_chart.data = lineData
        var xAxis = rootView.line_chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisValueFormatter()
    }

    private class MyAxisValueFormatter : ValueFormatter() {
        val format: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-M-yyyy")
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            val longV = value.toLong()
            val localDate = LocalDate.ofEpochDay(longV)
            return localDate.format(format)
        }

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val longV = value.toLong()
            val localDate = LocalDate.ofEpochDay(longV)
            return localDate.format(format)
        }

    }


    companion object{
        private const val scoresKey = "scores"
        @JvmStatic
        fun newInstance(scores: ArrayList<UserScore>) : LineChartFragment =
            LineChartFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(scoresKey,scores)
                }
            }
    }
}