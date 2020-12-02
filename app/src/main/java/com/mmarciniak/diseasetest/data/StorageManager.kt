package com.mmarciniak.diseasetest.data

import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class StorageManager() {
    private lateinit var dbName: String
    private lateinit var dbRef: DatabaseReference
    private val userScores = mutableListOf<UserScore>()

    constructor(databaseName: String) : this() {
        val database = FirebaseDatabase.getInstance()
        dbName = databaseName
        dbRef = database.getReference(dbName)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(user in dataSnapshot.children)
                {
                    for(scoreDate in user.children) {
                        val userName: String = scoreDate.child("userName").value as String
                        val diseaseName: String = scoreDate.child("diseaseName").value as String
                        val score: Double = scoreDate.child("score").value as Double
                        userScores.add(UserScore(userName,diseaseName,score))
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }


    fun saveUserScoreForDisease(userId: String, userName: String, disease: String, score: Double) {
        val sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        dbRef.child(userId).child(currentDate).setValue(UserScore(userName, disease, score))
    }

}