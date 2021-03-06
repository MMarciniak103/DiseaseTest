package com.mmarciniak.diseasetest

import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import com.mmarciniak.diseasetest.data.StorageListener
import com.mmarciniak.diseasetest.data.StorageManager
import com.mmarciniak.diseasetest.data.UserScore
import com.mmarciniak.diseasetest.fragments.datavisualization.LineChartFragment
import com.mmarciniak.diseasetest.fragments.datavisualization.OnGraphClosedListener
import com.mmarciniak.diseasetest.fragments.datavisualization.PieChartFragment
import kotlinx.android.synthetic.main.activity_stats.*


class StatsActivity : AppCompatActivity(), StorageListener<UserScore> , OnGraphClosedListener {
    private val storageManager = StorageManager("usersScores")
    private val loadingDialog = LoadingDialog(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empty_background)
        storageManager.registerListener(this)

        loadingDialog.startLoadingDialog()

    }

    override fun readData(data: List<UserScore>) {
        loadingDialog.dismissDialog()
        setContentView(R.layout.activity_stats)
        var bestScore = UserScore("default","no data",0.0,"")
        var worstScore = UserScore("default","no data",1.0,"")

        pie_chart_button.setOnClickListener{
            val pieChartDialog = PieChartFragment.newInstance(ArrayList(data))
            pieChartDialog.show(supportFragmentManager, "pieChartDialog")
        }

        line_chart_button.setOnClickListener{
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            val lineChartDialog = LineChartFragment.newInstance(ArrayList(data))
            lineChartDialog.show(supportFragmentManager,"lineChartDialog")
        }

        data.forEach { userScore ->
            if(userScore.score > bestScore.score)
                bestScore = userScore
            else if(userScore.score < worstScore.score)
                worstScore = userScore
        }

        // Set labels that tells about user's best and worst scores
        val builder1 = SpannableStringBuilder("Your best score is:\n")
        builder1.append(prepareTextInput("${bestScore.diseaseName} - ${bestScore.score*100}%",true,R.color.correctAnswer))
        best_score_tv.text = builder1


        val builder2 = SpannableStringBuilder("Your worst score is:\n")
        builder2.append(prepareTextInput("${worstScore.diseaseName} - ${worstScore.score*100}%",true,R.color.wrongAnswer))
        worst_score_tv.text = builder2

        val numLabelText = "Total number of taken tests:\n${data.count()}"
        num_tests_tv.text = numLabelText
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

    override fun onComplete() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    }


}