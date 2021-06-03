package com.summermessenger.data.repository

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

    // in-memory cache of the loggedInUser object
    var loggedInUser: User? = null
        private set

    val isLoggedIn: Boolean
        get() = loggedInUser != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        loggedInUser = null
    }

    fun logout() {
        loggedInUser = null
        FirebaseData.Auth.signOut()
    }

    suspend fun loginDefault(){
        loggedInUser = usersDao.tryGetUser(FirebaseData.Auth.currentUser)
    }

    suspend fun login(username: String, password: String): Result<User> {
        // handle login
        val result = usersDao.getUser(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(loggedInUser: User) {
        this.loggedInUser = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}