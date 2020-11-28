package com.mmarciniak.diseasetest.api

import okhttp3.*
import java.io.IOException

class DiseaseApiManager : ApiManager() {
    private val baseUrl = "http://192.168.0.196/"

     var selectedDisease : String? = null

    fun getListOfDiseases(callback: (m: String) -> Unit) {
        val url = baseUrl + "diseases"
        fun onResponse(call: Call, response: Response) {
            val body = response.body?.string()
            body?.let { callback(it) }
        }

        fun onFailure(call: Call, e: IOException) {
            println("Failed to execute request: " + e.printStackTrace())
        }
        createGetRequest(url, ::onResponse,::onFailure)
    }

    fun getRandomQuestion(callback: (m: String) -> Unit, diseaseName: String) {
        val url = baseUrl + "question?disease=$diseaseName"
        fun onResponse(call: Call, response: Response) {
            val body = response.body?.string()
            body?.let { callback(it) }
        }
        fun onFailure(call: Call, e: IOException) {
            println("Failed to execute request: " + e.printStackTrace())
        }

    }
}