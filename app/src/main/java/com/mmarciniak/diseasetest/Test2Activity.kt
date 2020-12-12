package com.mmarciniak.diseasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmarciniak.diseasetest.api.DiseaseApiManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.activity_test2.*
import java.io.IOException
import java.util.*

class Test2Activity : AppCompatActivity() {
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
        apiManager.getDiseaseSymptoms(::handleDiseaseSymptoms,randomDisease)
    }


    private fun handleDiseaseSymptoms(symptoms: Array<String>) {

        runOnUiThread {
            symptoms_recycler_view.layoutManager = LinearLayoutManager(this)
            symptoms_recycler_view.adapter = Test2Adapter(symptoms.asList())
        }
    }

    private fun submitAnswer() {
        if (disease_input.text.trim().toString().isNotEmpty())
        {
            val diseaseName = disease_input.text.trim().toString().toLowerCase(Locale.getDefault())
            val goodAnswer = apiManager.selectedDisease?.trim()?.toLowerCase(Locale.getDefault())
            goodAnswer?.let {
                val words = goodAnswer.split(" ").toList()
                val rx = Regex("\\b(?:${words.joinToString(separator="|")})\\b")
                val matches = rx.containsMatchIn(diseaseName)
                if(matches)
                {

                }
            }
        }
        else{
            Toast.makeText(this,"You need to enter disease name",Toast.LENGTH_SHORT).show()
        }
    }

}