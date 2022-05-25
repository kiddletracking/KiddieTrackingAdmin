package com.example.kiddietrackingadmin.daos

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DriverDao {
    private val db = Firebase.firestore
    val driverCollection = db.collection("Driver")
}