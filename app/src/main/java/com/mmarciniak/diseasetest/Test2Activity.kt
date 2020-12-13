package com.mmarciniak.diseasetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmarciniak.diseasetest.api.DiseaseApiManager
import com.mmarciniak.diseasetest.fragments.OnQuizCompleteListener
import com.mmarciniak.diseasetest.fragments.QuizResultDialogFragment
import com.mmarciniak.diseasetest.fragments.Test2ResultDialogFragment
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.activity_test2.*
import java.io.IOException
import java.util.*

class Test2Activity : AppCompatActivity(), OnQuizCompleteListener {
    private val apiManager = DiseaseApiManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        try {
            apiManager.getListOfDiseases(::chooseRandomDisease)
        } catch (e: IOException) {
            println(e.printStackTrace())
        }

        submit_disease_btn.setOnClickListener {
            submitAnswer()
        }
    }

    private fun chooseRandomDisease(diseasesList: Array<String>) {
        val randomDisease = diseasesList.random()
        apiManager.selectedDisease = randomDisease
        println("[SECOND TEST] Selected Disease: $randomDisease")
        apiManager.getDiseaseSymptoms(::handleDiseaseSymptoms, randomDisease)
    }


    private fun handleDiseaseSymptoms(symptoms: Array<String>) {

        runOnUiThread {
            symptoms_recycler_view.layoutManager = LinearLayoutManager(this)
            symptoms_recycler_view.adapter = Test2Adapter(symptoms.asList())
        }
    }

    private fun submitAnswer() {
        if (disease_input.text.trim().toString().isNotEmpty()) {
            val diseaseName = disease_input.text.trim().toString().toLowerCase(Locale.getDefault())
            val goodAnswer = apiManager.selectedDisease?.trim()?.toLowerCase(Locale.getDefault())
            goodAnswer?.let {
                val match = checkIfMatch(goodAnswer, diseaseName)
                val dialog = Test2ResultDialogFragment.newInstance(goodAnswer, match)
                dialog.show(supportFragmentManager, "test2ResultDialog")
            }
        } else {
            Toast.makeText(this, "You need to enter disease name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfMatch(goodAnswer: String, diseaseName: String): Boolean {
        val words = goodAnswer.split(" ").toList()
        val rx = Regex("\\b(?:${words.joinToString(separator = "|")})\\b")
        val matchesNum = rx.findAll(diseaseName)
        var match = false
        if (matchesNum.count() == words.size) match = true
        return match
    }

    override fun onComplete(reset: Boolean) {
        disease_input.text.clear()
        try {
            apiManager.getListOfDiseases(::chooseRandomDisease)
        } catch (e: IOException) {
            println(e.printStackTrace())
        }
        if (reset)
            apiManager.selectedDisease?.let { disease ->
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra(urlKey, disease)
                startActivity(intent)
            }

    }

    companion object {
        const val urlKey = "urlKey";
    }
}