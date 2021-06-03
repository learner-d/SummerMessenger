package com.summermessenger.data.db

import com.google.firebase.firestore.DocumentSnapshot
import com.summermessenger.data.db.FireStoreDb
import com.summermessenger.data.model.Chat
import com.summermessenger.data.model.User
import com.summermessenger.util.await

class ChatsDao(val db:FireStoreDb) {
    fun getChats() : List<Chat>{
        TODO()
    }
    suspend fun getChat(chatId:Int) : Chat{
        val chatDoc: DocumentSnapshot = db.chats.document(chatId.toString()).get().await()
        return Chat.load(chatDoc)
    }
    fun deleteChat(chatId: Int){
        TODO()
    }
}