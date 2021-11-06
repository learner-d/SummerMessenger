package com.summermessenger.data.model

import com.google.firebase.firestore.DocumentSnapshot

data class User(
    var username: String,
    var displayName: String,
    var phoneNumber:String) {

    companion object{
        fun load(userDoc:DocumentSnapshot): User{
            val udUsername = userDoc["username"] ?: throw IllegalStateException("userDoc has no username!")
            val username = udUsername as String

            val displayName = userDoc["displayName"] as String? ?: ""
            val phoneNumber = userDoc["phoneNumber"] as String? ?: ""
            return User(username,displayName,phoneNumber)
        }
    }
}