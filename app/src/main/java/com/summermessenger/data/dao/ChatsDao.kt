package com.summermessenger.data.dao

import com.summermessenger.data.model.Chat
import com.summermessenger.data.model.User

abstract class ChatsDao {
    abstract fun getChats() : List<Chat>
    abstract fun getChat(chatId:Int) : Chat
    abstract fun deleteChat(chatId: Int)
}