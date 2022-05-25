package com.example.kiddietrackingadmin.models

data class User(
    val uid: String = "",
    val name: String? = "",
    val image: String? = "",
    val classStd: String? = "",
    val parentName: String? = "",
    val parentContact: String? = "",
    val driverUid: String? = ""
)
