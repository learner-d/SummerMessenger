package com.summermessenger.data

class MainRepository {
    companion object{
        val loginDataSource = LoginDataSource()
        val loginRepository = LoginRepository(loginDataSource)
    }
}