package com.example.kiddietrackingadmin.daos

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserDao {
    private val db = Firebase.firestore
    val userCollection = db.collection("Users")
}