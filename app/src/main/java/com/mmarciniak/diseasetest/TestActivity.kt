package com.mmarciniak.diseasetest

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.mmarciniak.diseasetest.api.DiseaseApiManager
import com.mmarciniak.diseasetest.data.QuestionData
import com.mmarciniak.diseasetest.data.QuestionDataContainer
import kotlinx.android.synthetic.main.activity_test.*
import java.io.IOException
import java.util.*


class TestActivity : AppCompatActivity() {
    private val apiManager = DiseaseApiManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        try {
            apiManager.getListOfDiseases(::chooseRandomDisease)
        } catch (e: IOException) {
            println(e.printStackTrace())
        }

    }

    private fun chooseRandomDisease(diseasesList: Array<String>) {
        val randomDisease = diseasesList.random()
        val displayText = "Select symptoms of given disease:\n $randomDisease"
        apiManager.selectedDisease = randomDisease
        apiManager.getRandomQuestion(::populateTestView, randomDisease)
        runOnUiThread() {
            disease_tv.text = displayText
        }
    }

    private fun populateTestView(questionDataContainer: QuestionDataContainer) {
        println(Arrays.toString(questionDataContainer.falseSymptoms))
        println(Arrays.toString(questionDataContainer.trueSymptoms))
        val trueIds = questionDataContainer.trueSymptoms.indices
        val symptoms = createQuestionsData(questionDataContainer)

        val shuffledSymptoms = symptoms.shuffled()

        runOnUiThread() {
            var k = 0
            for (i in 0 until test_grid.childCount) {
                val v: View = test_grid.getChildAt(i)
                if (v is CardView) {
                    val llayout: View = v.getChildAt(0)
                    if (llayout is LinearLayout) {
                        for (j in 0 until llayout.childCount) {
                            val cardContent: View = llayout.getChildAt(j)
                            if (cardContent is TextView) {

                                cardContent.text = shuffledSymptoms[k].symptom.replace("_"," ")
                                k++
                            }
                        }
                    }

                }
            }
        }
    }

    private fun createQuestionsData(questionDataContainer: QuestionDataContainer): List<QuestionData> {
        val symptoms: MutableList<QuestionData> = mutableListOf()
        questionDataContainer.trueSymptoms.forEachIndexed { i, symptom ->
            symptoms.add(QuestionData(i, symptom))
        }
        questionDataContainer.falseSymptoms.forEach { symptom ->
            val index = symptoms.size
            symptoms.add(QuestionData(index, symptom))
        }
        return symptoms
    }

}