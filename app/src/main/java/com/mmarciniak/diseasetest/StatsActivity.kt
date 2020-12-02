package com.mmarciniak.diseasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mmarciniak.diseasetest.data.StorageListener
import com.mmarciniak.diseasetest.data.StorageManager
import com.mmarciniak.diseasetest.data.UserScore

class StatsActivity : AppCompatActivity(), StorageListener<UserScore> {
    private val storageManager = StorageManager("usersScores")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        storageManager.registerListener(this)

    }

    override fun readData(data: List<UserScore>) {
        data.forEach { userScore ->
            println(userScore)
        }
    }
}