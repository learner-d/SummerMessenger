package com.summermessenger.data.db

import com.summermessenger.data.db.FireStoreDb
import com.summermessenger.data.model.Chat
import com.summermessenger.data.model.Message

class MessagesDao(val db: FireStoreDb) {
    fun getMessages(chat:Chat) : List<Message>{
        TODO()
    }
    fun getMessage(messageId:Int) : Message{
        TODO()
    }
    fun deleteMessage(messageId: Int){
        TODO()
    }
}