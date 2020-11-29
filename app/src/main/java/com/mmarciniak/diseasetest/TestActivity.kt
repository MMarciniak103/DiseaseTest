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
    private val selectedTiles = mutableListOf<Int>()
    lateinit var trueIds: List<Int>

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
        // get question for given disease
        apiManager.getRandomQuestion(::populateTestView, randomDisease)
        // register handler for info icon that opens dialog with disease description
        apiManager.getDiseaseDescription(::addDiseaseDescriptionHandler, randomDisease)

        runOnUiThread() {
            disease_tv.text = displayText
        }
    }

    private fun populateTestView(questionDataContainer: QuestionDataContainer) {
        println(Arrays.toString(questionDataContainer.falseSymptoms))
        println(Arrays.toString(questionDataContainer.trueSymptoms))
        trueIds = questionDataContainer.trueSymptoms.indices.toList()
        val symptoms = createQuestionsData(questionDataContainer)

        val shuffledSymptoms = symptoms.shuffled()

        runOnUiThread() {
            // Iterate over grid view and handle events associated with child views
            // Its structure is in form: grid view -> card views -> linear layout -> [ image view, image view, text view]
            var k = 0
            for (i in 0 until test_grid.childCount) {
                val cardView: View = test_grid.getChildAt(i)
                if (cardView is CardView) {
                    val llayout: View = cardView.getChildAt(0)
                    if (llayout is LinearLayout) {
                        addCardViewOnClick(cardView,i, llayout,symptoms)
                        for (j in 0 until llayout.childCount) {
                            val cardContent: View = llayout.getChildAt(j)
                            if (cardContent is TextView) {

                                cardContent.text = shuffledSymptoms[k].symptom.replace("_", " ")
                                k++
                            }
                        }
                    }
                }
            }
        }
    }

    private fun addCardViewOnClick(cardView: View, i: Int, llayout: View,symptoms: List<QuestionData>) {
        cardView.setOnClickListener {
            // if cardview was selected -> unselect
            if (symptoms[i].id in selectedTiles) {
                llayout.setBackgroundResource(R.drawable.border)
                selectedTiles.remove(symptoms[i].id)
            } else {
                // else if it was unselected -> select
                llayout.setBackgroundResource(0)
                selectedTiles.add(symptoms[i].id)
            }
            println("SELECTED TILES $selectedTiles")
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

    private fun addDiseaseDescriptionHandler(diseaseDescription: String, diseaseName: String) {
        runOnUiThread {
            diseaseDescription_info.setOnClickListener {
                val dialog = CustomDialogFragment.newInstance(diseaseName, diseaseDescription)
                dialog.show(supportFragmentManager, "customDialog")
            }
        }
    }


    private fun submitTest() {
        var correctNums = 0
        // check how many true symptoms were selected
        trueIds.forEach {
            if (it in selectedTiles) {
                correctNums++
            }
        }


    }
}