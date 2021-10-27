package com.summermessenger.auth

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.summermessenger.data.FirebaseData
import com.summermessenger.data.Result
import com.summermessenger.data.model.User
import com.summermessenger.data.repository.MainRepository
import com.summermessenger.data.repository.UsersRepository
import kotlinx.coroutines.tasks.await

class LoginManager() {
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
    suspend fun createUser(email: String, password: String): FirebaseUser? {
        try {
            val authResult = FirebaseData.Auth.createUserWithEmailAndPassword(email, password).await()
            return authResult.user
        } catch (e: Exception) {
            Log.d(TAG, "signInWithCustomToken:failure", e)
            // Обробити помилку, можливо, треба сповістити користувача
            return null
        }
    }

    suspend fun login(username: String, password: String): Result<User> {
        // handle login
        val result = MainRepository.usersRepository.getUser(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    fun logout() {
        loggedInUser = null
        FirebaseData.Auth.signOut()
    }

    suspend fun updateCurrentUser(){
        loggedInUser = MainRepository.usersRepository.tryGetUser(FirebaseData.Auth.currentUser)
    }

    private fun setLoggedInUser(loggedInUser: User) {
        this.loggedInUser = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}