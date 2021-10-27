package com.summermessenger.data.repository

import com.google.firebase.auth.FirebaseUser
import com.summermessenger.data.FirebaseData
import com.summermessenger.data.Result
import com.summermessenger.data.db.UsersDao
import com.summermessenger.data.model.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class UsersRepository private constructor (private val usersDao: UsersDao) {
    companion object {
        private var _instance:UsersRepository? = null
        fun getInstance(usersDao: UsersDao) = _instance ?: synchronized(this){
            _instance ?: UsersRepository(usersDao)
        }
    }

    suspend fun tryGetUser(fbUser: FirebaseUser?): User? {
        return usersDao.tryGetUser(fbUser)
    }

    suspend fun getUser(username:String, password:String) : Result<User> {
        return usersDao.getUser(username, password)
    }
}