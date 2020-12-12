package com.mmarciniak.diseasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.mmarciniak.diseasetest.api.DiseaseApiManager
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.activity_test2.*
import java.io.IOException

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
    }

    private fun chooseRandomDisease(diseasesList: Array<String>) {
        val randomDisease = diseasesList.random()
        apiManager.selectedDisease = randomDisease

        apiManager.getDiseaseSymptoms(::handleDiseaseSymptoms,randomDisease)
    }


    private fun handleDiseaseSymptoms(symptoms: Array<String>) {

        runOnUiThread {
            symptoms_recycler_view.layoutManager = LinearLayoutManager(this)
            symptoms_recycler_view.adapter = Test2Adapter(symptoms.asList())
        }
    }

}