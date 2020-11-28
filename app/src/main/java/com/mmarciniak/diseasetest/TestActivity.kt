package com.mmarciniak.diseasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.GsonBuilder
import com.mmarciniak.diseasetest.api.DiseaseApiManager
import kotlinx.android.synthetic.main.activity_test.*
import java.io.IOException

class TestActivity : AppCompatActivity() {
    private val apiManager = DiseaseApiManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        try {
            apiManager.getListOfDiseases(::chooseRandomDisease)
        }
        catch (e : IOException){
            println(e.printStackTrace())
        }

    }

    private fun chooseRandomDisease(jsonBody : String)
    {
        val gson = GsonBuilder().create()
        val diseases = gson.fromJson(jsonBody,Array<String>::class.java)

        val randomDisease = diseases.random()
        val displayText = "Select symptoms of given disease:\n $randomDisease"
        apiManager.selectedDisease = randomDisease
        apiManager.getRandomQuestion(::populateTestView,randomDisease)
        runOnUiThread() {
            disease_tv.text = displayText
        }
    }

    private fun populateTestView(jsonBody: String)
    {
        
    }

}