package com.summermessenger.data.repository

import com.summermessenger.data.db.FireStoreDb

class MainRepository {
    companion object{
        val fireStoreDb = FireStoreDb.instance
        val usersRepository = UsersRepository.getInstance(fireStoreDb.usersDao)
        val chatsRepository = ChatsRepository.getInstance(fireStoreDb.chatsDao)
    }
}