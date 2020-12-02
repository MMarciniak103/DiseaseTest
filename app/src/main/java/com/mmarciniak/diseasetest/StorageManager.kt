package com.mmarciniak.diseasetest

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class StorageManager(databaseName: String) {
    private var database = FirebaseDatabase.getInstance()
    private var dbName: String = databaseName

    public fun saveUserScoreForDisease(userName: String, disease: String, score: Float) {
        var dbRef = database.getReference(dbName)
        dbRef.child(userName)
    }
}