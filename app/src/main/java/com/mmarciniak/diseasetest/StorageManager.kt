package com.mmarciniak.diseasetest

import com.google.firebase.database.FirebaseDatabase
import com.mmarciniak.diseasetest.data.UserScore


class StorageManager(databaseName: String) {
    private var database = FirebaseDatabase.getInstance()
    private var dbName: String = databaseName

    fun saveUserScoreForDisease(userId: String, userName: String, disease: String, score: Float) {
        var dbRef = database.getReference(dbName)
        dbRef.child(userId).setValue(UserScore(userName, disease, score))
    }
}