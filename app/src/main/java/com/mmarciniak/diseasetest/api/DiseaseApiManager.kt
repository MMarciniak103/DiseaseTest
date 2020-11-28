package com.mmarciniak.diseasetest.api

import com.google.gson.GsonBuilder
import com.mmarciniak.diseasetest.data.QuestionDataContainer
import okhttp3.*
import java.io.IOException

class DiseaseApiManager : ApiManager() {
    private val baseUrl = "http://192.168.0.196/"

     var selectedDisease : String? = null

    fun getListOfDiseases(callback: (m: Array<String>) -> Unit) {
        val url = baseUrl + "diseases"
        fun onResponse(call: Call, response: Response) {
            val body = response.body?.string()
            val gson = GsonBuilder().create()
            val diseases = gson.fromJson(body,Array<String>::class.java)
            callback(diseases)
        }
        fun onFailure(call: Call, e: IOException) {
            println("Failed to execute request: " + e.printStackTrace())
        }
        createGetRequest(url, ::onResponse,::onFailure)
    }

    fun getRandomQuestion(callback: (m: QuestionDataContainer) -> Unit, diseaseName: String) {
        val url = baseUrl + "question?disease=$diseaseName"
        fun onResponse(call: Call, response: Response) {
            val body = response.body?.string()
            val gson = GsonBuilder().create()
            val questionData = gson.fromJson(body,QuestionDataContainer::class.java)
            callback(questionData)
        }
        fun onFailure(call: Call, e: IOException) {
            println("Failed to execute request: " + e.printStackTrace())
        }

        createGetRequest(url,::onResponse,::onFailure)
    }
}