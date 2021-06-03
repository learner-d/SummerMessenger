package com.summermessenger.data.repository

import com.summermessenger.data.db.ChatsDao
import com.summermessenger.data.db.UsersDao

class ChatsRepository private constructor(private val chatsDao: ChatsDao){
    companion object{
        private var _instance:ChatsRepository? = null
        fun getInstance(chatsDao: ChatsDao) = _instance ?: synchronized(this){
            _instance ?: ChatsRepository(chatsDao)
        }
    }

    suspend fun getChat(chatId:Int) = chatsDao.getChat(chatId)
}