package com.mmarciniak.diseasetest

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.mmarciniak.diseasetest.data.StorageListener
import com.mmarciniak.diseasetest.data.StorageManager
import com.mmarciniak.diseasetest.data.UserScore
import kotlinx.android.synthetic.main.activity_stats.*


class StatsActivity : AppCompatActivity(), StorageListener<UserScore> {
    private val storageManager = StorageManager("usersScores")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        storageManager.registerListener(this)

    }

    override fun readData(data: List<UserScore>) {
        var bestScore = UserScore("default","no data",0.0)
        var worstScore = UserScore("default","no data",1.0)
        data.forEach { userScore ->
            if(userScore.score > bestScore.score)
                bestScore = userScore
            else if(userScore.score < worstScore.score)
                worstScore = userScore
        }

        // Set labels that tells about user's best and worst scores
        val builder1 = SpannableStringBuilder("Your best score is:\n")
        builder1.append(prepareTextInput("${bestScore.diseaseName} - ${bestScore.score}%",true,R.color.correctAnswer))
        best_score_tv.text = builder1


        val builder2 = SpannableStringBuilder("Your worst score is:\n")
        builder2.append(prepareTextInput("${worstScore.diseaseName} - ${worstScore.score}%",true,R.color.wrongAnswer))
        worst_score_tv.text = builder2

//        populateBarChart(data)
        
    }

    private fun prepareTextInput(text: String, boldStyle: Boolean = false,colorId: Int): SpannableString {
        val bestScoreInput = SpannableString(text)
        if(boldStyle){
            val boldSpan = StyleSpan(Typeface.BOLD)
            bestScoreInput.setSpan(boldSpan, 0, bestScoreInput.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); //To make text Bold
        }
        bestScoreInput.setSpan(ForegroundColorSpan(resources.getColor(colorId)), 0, bestScoreInput.length, 0)
        return bestScoreInput
    }

    private fun populateBarChart(scores: List<UserScore>){
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
//        pie_chart.data = pieData
//        pie_chart.description.text = ""
//        pie_chart.centerText = "No. diseases encountered"
//        pie_chart.animate()
    }
}