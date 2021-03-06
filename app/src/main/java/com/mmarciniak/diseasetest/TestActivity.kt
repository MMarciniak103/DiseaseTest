package com.mmarciniak.diseasetest

import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import com.google.firebase.auth.FirebaseAuth
import com.mmarciniak.diseasetest.api.DiseaseApiManager
import com.mmarciniak.diseasetest.data.QuestionData
import com.mmarciniak.diseasetest.data.QuestionDataContainer
import com.mmarciniak.diseasetest.data.StorageManager
import com.mmarciniak.diseasetest.fragments.DiseaseInfoDialogFragment
import com.mmarciniak.diseasetest.fragments.OnQuizCompleteListener
import com.mmarciniak.diseasetest.fragments.QuizResultDialogFragment
import kotlinx.android.synthetic.main.activity_test.*
import java.io.IOException
import java.util.*


class TestActivity : AppCompatActivity(), DialogInterface.OnDismissListener, OnQuizCompleteListener {
    private val apiManager = DiseaseApiManager()
    private val storageManager = StorageManager("usersScores")
    private val selectedTiles = mutableListOf<Int>()
    private var trueIds: List<Int> = emptyList()
    private var shuffledSymptoms: List<QuestionData> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        try {
            apiManager.getListOfDiseases(::chooseRandomDisease)
        } catch (e: IOException) {
            println(e.printStackTrace())
        }

        submit_button.text = "CONFIRM"
        submit_button.setOnClickListener {
            submitTest()
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

        shuffledSymptoms = symptoms.shuffled()

        runOnUiThread() {
            // Iterate over grid view and handle events associated with child views
            // Its structure is in form: grid view -> card views -> linear layout -> [ image view, image view, text view]
            var k = 0
            for (i in 0 until test_grid.childCount) {
                val cardView: View = test_grid.getChildAt(i)
                if (cardView is CardView) {
                    val llayout: View = cardView.getChildAt(0)
                    if (llayout is LinearLayout) {
                        addCardViewOnClick(cardView, i, llayout, shuffledSymptoms)
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

    private fun addCardViewOnClick(
        cardView: View,
        i: Int,
        llayout: View,
        symptoms: List<QuestionData>
    ) {
        cardView.setOnClickListener {
            // if cardview was selected -> unselect
            if (symptoms[i].id in selectedTiles) {
                llayout.setBackgroundResource(0)
                selectedTiles.remove(symptoms[i].id)
            } else {
                // else if it was unselected -> select
                llayout.setBackgroundResource(R.drawable.border)
                changeStrokeColor(llayout, getColor(R.color.defaultStroke))
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
                val displayText = "Disease: $diseaseName"
                val dialog = DiseaseInfoDialogFragment.newInstance(displayText, diseaseDescription)
                dialog.show(supportFragmentManager, "customDialog")
            }
        }
    }


    private fun submitTest() {
        var correctNums = 0

        shuffledSymptoms.forEach {
            println(it)
            if (it.id in trueIds && it.id in selectedTiles) {
                correctNums++
            } else if (it.id !in trueIds && it.id !in selectedTiles) {
                correctNums++
            }
        }
        println("CORRECT SYMP: $trueIds")
        println("SELECTED : $selectedTiles")
        println("CORRECT : $correctNums")

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val uid = it.uid
            val email = it.email
            if (email != null && apiManager.selectedDisease != null)
                storageManager.saveUserScoreForDisease(
                    uid, email,
                    apiManager.selectedDisease!!, correctNums / 10.toDouble()
                )
        }
        val dialog = QuizResultDialogFragment.newInstance(correctNums)
        dialog.show(supportFragmentManager, "quizResultDialog")
    }

    private fun showAnswers() {
        for (i in 0 until test_grid.childCount) {
            val cardView: View = test_grid.getChildAt(i)
            if (cardView is CardView) {
                val llayout: View = cardView.getChildAt(0)
                if (llayout is LinearLayout) {
                    llayout.setBackgroundResource(R.drawable.border)
                    if (shuffledSymptoms[i].id in trueIds) {
                        changeStrokeColor(llayout, getColor(R.color.correctAnswer))
                        changeStatusIcon(llayout, R.drawable.checked, R.color.correctAnswer)
                    } else {
                        changeStrokeColor(llayout, getColor(R.color.wrongAnswer))
                        changeStatusIcon(llayout, R.drawable.close, R.color.wrongAnswer)
                    }
                }
            }
        }
    }

    private fun resetQuiz() {
        selectedTiles.clear()
        trueIds = emptyList()
        shuffledSymptoms = emptyList()
        resetTilesSelection()
        try {
            apiManager.getListOfDiseases(::chooseRandomDisease)
        } catch (e: IOException) {
            println(e.printStackTrace())
        }
    }

    private fun resetTilesSelection() {
        for (i in 0 until test_grid.childCount) {
            val cardView: View = test_grid.getChildAt(i)
            if (cardView is CardView) {
                val llayout: View = cardView.getChildAt(0)
                if (llayout is LinearLayout) {
                    changeStatusIcon(llayout, R.drawable.info, R.color.pastelBlue)
                    llayout.setBackgroundResource(0)
                }

            }
        }
    }

    private fun changeStrokeColor(view: View, color: Int) {
        val backgroundGradient = view.background as GradientDrawable
        val sp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            5f,
            resources.displayMetrics
        )
        backgroundGradient.setStroke(sp.toInt(), color)
    }

    private fun changeStatusIcon(llayout: LinearLayout, icon: Int, color: Int) {
        val imageIcon = llayout.getChildAt(0) as ImageView
        imageIcon.setImageResource(icon)
        val myColor = ContextCompat.getColor(applicationContext, color)
        ImageViewCompat.setImageTintList(imageIcon, ColorStateList.valueOf(myColor));

    }

    override fun onDismiss(p0: DialogInterface?) {
        finish()
    }

    override fun onComplete(reset: Boolean) {
        if (reset) resetQuiz()
        else {
            showAnswers()
            submit_button.text = "RETRY"
            submit_button.setOnClickListener {
                resetQuiz()
                submit_button.text = "CONFIRM"
                submit_button.setOnClickListener {
                    submitTest()
                }
            }
        }
    }

}