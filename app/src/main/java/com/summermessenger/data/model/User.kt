package com.summermessenger.data.model

import com.google.firebase.firestore.DocumentSnapshot
import java.io.Serializable

data class User (
    val userId: String = "",
    var username: String = "",
    var displayName: String = "",
    var phoneNumber:String = "") : Serializable {

    companion object{
        fun load(userDoc:DocumentSnapshot): User? {
            return userDoc.toObject(User::class.java)
        }
    }
}