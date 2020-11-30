package com.mmarciniak.diseasetest.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mmarciniak.diseasetest.data.QuestionDataContainer
import okhttp3.*
import java.io.IOException

class DiseaseApiManager : ApiManager() {
    private val baseUrl = "http://192.168.0.196/"

    var selectedDisease: String? = null
    private val gson: Gson = GsonBuilder().create()


    fun getListOfDiseases(callback: (m: Array<String>) -> Unit) {
        val url = baseUrl + "diseases"
        fun onResponse(call: Call, response: Response) {
            val body = response.body?.string()
            val diseases = gson.fromJson(body, Array<String>::class.java)
            callback(diseases)
        }

        fun onFailure(call: Call, e: IOException) {
            println("Failed to execute request: " + e.printStackTrace())
        }
        createGetRequest(url, ::onResponse, ::onFailure)
    }

    fun getRandomQuestion(callback: (m: QuestionDataContainer) -> Unit, diseaseName: String) {
        val url = baseUrl + "question?disease=$diseaseName"
        fun onResponse(call: Call, response: Response) {
            val body = response.body?.string()
            println(body)
            val questionData = gson.fromJson(body, QuestionDataContainer::class.java)
            callback(questionData)
        }
        fun onFailure(call: Call, e: IOException) {
            println("Failed to execute request: " + e.printStackTrace())
        }

        createGetRequest(url, ::onResponse, ::onFailure)
    }

    fun getDiseaseDescription(callback: (m: String,n: String) -> Unit, diseaseName: String) {
        val url = baseUrl + "disease/description?disease=$diseaseName"
        fun onResponse(call: Call, response: Response) {
            val body = response.body?.string()
            println(body)
            val diseaseDescription = gson.fromJson(body, String::class.java)
            callback(diseaseDescription,diseaseName)
        }
        fun onFailure(call: Call, e: IOException) {
            println("Failed to execute request: " + e.printStackTrace())
        }
        createGetRequest(url, ::onResponse, ::onFailure)
    }
}