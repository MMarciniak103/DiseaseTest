package com.mmarciniak.diseasetest.api

import okhttp3.*
import java.io.IOException

open class ApiManager {
    fun createGetRequest(
        url: String, onResponse: (call: Call, response: Response) -> Unit, onFailure: (call: Call, e: IOException) -> Unit) {
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                onResponse(call, response)
            }

            override fun onFailure(call: Call, e: IOException) {
                onFailure(call, e)
            }
        })
    }
}