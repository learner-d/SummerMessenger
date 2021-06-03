package com.summermessenger.data.dao

import com.summermessenger.data.model.Chat
import com.summermessenger.data.model.Message

abstract class MessagesDao {
    abstract fun getMessages(chat:Chat) : List<Message>
    abstract fun getMessage(messageId:Int) : Message
    abstract fun deleteMessage(messageId: Int)
}