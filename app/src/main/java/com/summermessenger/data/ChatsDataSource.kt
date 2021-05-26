package com.summermessenger.data

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.summermessenger.data.model.Chat
import com.summermessenger.util.await

class ChatsDataSource(fireStoreDb: FireStoreDb) {
    val chatsDbCollection = fireStoreDb.chats
    suspend fun loadChat(id:String): Chat{
        var chatDoc: DocumentSnapshot? = chatsDbCollection.document(id).get().await()
        return Chat.load(chatDoc!!)
    }
}