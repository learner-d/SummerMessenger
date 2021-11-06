package com.summermessenger.data.repository

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.summermessenger.data.FirebaseData
import com.summermessenger.data.Result
import com.summermessenger.data.db.UsersFbDao
import com.summermessenger.data.model.User
import kotlinx.coroutines.tasks.await

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class UsersRepository private constructor (private val usersFbDao: UsersFbDao) {
    companion object {
        private var _instance:UsersRepository? = null
        fun getInstance(usersFbDao: UsersFbDao) = _instance ?: synchronized(this){
            _instance ?: UsersRepository(usersFbDao)
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

    //реєстрація користувача: email і пароль
    suspend fun createUser(email: String, password: String): Result<User> {
        var authResult: AuthResult? = null
        var fbUser: FirebaseUser? = null
        try {
            authResult = FirebaseData.Auth.createUserWithEmailAndPassword(email, password).await()
            fbUser = authResult.user!!
            // Створити запис на користувача в базі даних
            val userDbRecord = createUser(fbUser.uid)
            return Result.Success(userDbRecord!!)
        } catch (e: Exception) {
            // видалити користувача
            if (fbUser != null) {
                fbUser.delete().await()
                Log.d(ContentValues.TAG, "${fbUser.uid} is deleted")
            }
            Log.e(ContentValues.TAG, "signInWithCustomToken:failure", e)
            // Обробити помилку, можливо, треба сповістити користувача
            return Result.Error(e)
        }
    }

    suspend fun login(username: String, password: String): Result<User> {
        // handle login
        val authResult: AuthResult?
        val fbUser: FirebaseUser?
        try {
            // перевірити вхідні дані ?
            // виконати автентифікацію у Firebase
            authResult = FirebaseData.Auth.signInWithEmailAndPassword(username, password).await()
            fbUser = authResult.user
            if (fbUser == null)
                return Result.Error(Exception("Користувача не авторизовано"))
            // отримати дані користувача із бази даних
            val userData = getUser(fbUser.uid)
                ?: return Result.Error(Exception("Користувача не знайдено в базі даних"))
            return Result.Success(userData)
        }
        catch (e: Exception) {
            Log.e(ContentValues.TAG, "login failure", e)
            return Result.Error(e)
        }
    }

    fun logout() {
        loggedInUser = null
        FirebaseData.Auth.signOut()
    }

    suspend fun updateCurrentUser() : Boolean {
        if (FirebaseData.Auth.currentUser?.uid != null) {
            loggedInUser = getUser(FirebaseData.Auth.currentUser!!.uid)
            return true
        }
        return false
    }

    private fun setLoggedInUser(loggedInUser: User) {
        this.loggedInUser = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    suspend fun getUser(userId: String): User? {
        return usersFbDao.getUser(userId)
    }
    @Deprecated("Should not be used for now")
    suspend fun getUser(username:String, password:String) : Result<User> {
        return usersFbDao.getUser(username, password)
    }
    suspend fun createUser(userId:String, displayName:String = "", phoneNum: String = ""): User? {
        return usersFbDao.createUser(userId, displayName, phoneNum)
    }
}