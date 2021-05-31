package com.summermessenger.data.model

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.model.Document

data class User(
    var username: String,
    var displayName: String,
    var password: String,
    var phoneNumber:String) {

    companion object{
        suspend fun load(userDoc:DocumentSnapshot): User{
            val udUsername = userDoc["username"]
            if (udUsername == null) throw IllegalStateException("userDoc has no username!")
            val username = udUsername as String

            val udPassword = userDoc["password"]
            if (udPassword == null) throw IllegalStateException("userDoc has no password!")
            val password = udPassword as String

            val displayName = userDoc["displayName"] as String? ?: ""
            val phoneNumber = userDoc["phoneNumber"] as String? ?: ""
            return User(username,displayName,password, phoneNumber)
        }
    }
}