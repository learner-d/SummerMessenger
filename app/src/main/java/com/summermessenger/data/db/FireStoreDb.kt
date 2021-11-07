package com.summermessenger.data.db

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FireStoreDb private constructor() {
    companion object{
        @Volatile
        private var _instance:FireStoreDb? = null
        val instance
            get() = _instance ?: synchronized(this){
                    _instance?: FireStoreDb().also { _instance = it }
                }
    }

    val db = Firebase.firestore
    val users = db.collection("users")
    val chats = db.collection("chats")
    val messages = db.collection("messages")

    val usersDao = UsersFbDao(this)
    val chatsDao = ChatsDao(this)
    val messagesDao = MessagesDao(this)
}