package com.summermessenger.data

import com.summermessenger.data.db.FireStoreDb

class MainRepository {
    companion object{
        val fireStoreDb = FireStoreDb()
        val loginDataSource = UsersDataSource(fireStoreDb)
        val loginRepository = LoginRepository(loginDataSource)
        
        val chatsDataSource = ChatsDataSource(fireStoreDb)
        val messagesDataSource = MessagesDataSource(fireStoreDb)
    }
}