package com.summermessenger.data

import com.google.firebase.firestore.DocumentSnapshot
import com.summermessenger.data.db.FireStoreDb
import com.summermessenger.data.model.Chat
import com.summermessenger.util.await

class ChatsDataSource(fireStoreDb: FireStoreDb) {
    val chatsDbCollection = fireStoreDb.chats
    suspend fun loadChat(id:String): Chat{
        val chatDoc: DocumentSnapshot? = chatsDbCollection.document(id).get().await()
        return Chat.load(chatDoc!!)
    }
}