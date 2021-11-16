package com.summermessenger.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.tasks.await

data class Message(val sender: User, val text: String, val timeStamp: Timestamp) {
    companion object{
        suspend fun load(msgDoc: DocumentSnapshot) : Message {
            val mdSenderRef = msgDoc["sender"] as DocumentReference
            val sender = User.load(mdSenderRef.get().await())

            val mdText = msgDoc["text"] as String

            val mdTimestamp = msgDoc["timeStamp"] as Timestamp

            return Message(sender!!, mdText, mdTimestamp)
        }
    }
}