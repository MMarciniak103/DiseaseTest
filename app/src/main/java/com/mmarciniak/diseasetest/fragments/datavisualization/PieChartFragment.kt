package com.mmarciniak.diseasetest.fragments.datavisualization

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.mmarciniak.diseasetest.R
import com.mmarciniak.diseasetest.data.UserScore
import kotlinx.android.synthetic.main.pie_chart_dialog.view.*
import java.util.*


class PieChartFragment : DialogFragment() {
    private lateinit var pieData: ArrayList<UserScore>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pieData = arguments?.getParcelableArrayList<UserScore>(scoresKey)
            ?: throw IllegalStateException("No args provided")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.pie_chart_dialog, container, false)

        populateBarChart(pieData,rootView)

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

    private fun populateBarChart(scores: List<UserScore>,rootView: View){
        val scoresMap = mutableMapOf<String,Int>()
        for (score in scores){
            val value = scoresMap[score.diseaseName] ?: 0
            scoresMap[score.diseaseName] = value + 1
        }
        val pieEntries = mutableListOf<PieEntry>()
        for((key,value) in scoresMap)
        {
            pieEntries.add(PieEntry(value.toFloat(),key))
        }
        val pieDataSet = PieDataSet(pieEntries,"Diseases")
        pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.asList()
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 12f

        val pieData = PieData(pieDataSet)
        rootView.pie_chart.data = pieData
        rootView.pie_chart.legend.isEnabled = false
        rootView.pie_chart.description.isEnabled = false
        rootView.pie_chart.setDrawEntryLabels(true)
        rootView.pie_chart.setEntryLabelTextSize(8f)
        rootView.pie_chart.centerText = "No. diseases encountered"
        rootView.pie_chart.animate()
    }


    companion object{
        private const val scoresKey = "scores"
        @JvmStatic
        fun newInstance(scores: ArrayList<UserScore>) : PieChartFragment =
            PieChartFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(scoresKey,scores)
                }
            }
    }
}