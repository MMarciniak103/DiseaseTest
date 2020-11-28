package com.mmarciniak.diseasetest.api

import okhttp3.*
import java.io.IOException

class DiseaseApiManager {
    private val baseUrl = "http://192.168.0.196/"

    fun getListOfDiseases(callback: (m: String) -> Unit)
    {
        val url = baseUrl+"diseases"
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                body?.let { callback(it) }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request: "+e.printStackTrace())
            }
        })
    }
}