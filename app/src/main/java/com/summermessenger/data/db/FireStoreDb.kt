package com.summermessenger.data.db

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FireStoreDb private constructor() {
    companion object{
        val instance = FireStoreDb()
    }

    val db = Firebase.firestore
    val users = db.collection("users")
    val chats = db.collection("chats")
    val messages = db.collection("messages")
}