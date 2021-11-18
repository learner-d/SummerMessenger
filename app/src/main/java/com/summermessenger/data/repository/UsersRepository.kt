package com.summermessenger.data.repository

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.summermessenger.data.FirebaseData
import com.summermessenger.data.Result
import com.summermessenger.data.db.UsersFbDao
import com.summermessenger.data.model.User
import com.summermessenger.ui.login.ELoginState
import com.summermessenger.ui.login.LoginResult
import kotlinx.coroutines.tasks.await

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class UsersRepository private constructor (private val usersFbDao: UsersFbDao) {
    companion object {
        private var oInstance:UsersRepository? = null
        fun getInstance(usersFbDao: UsersFbDao) = oInstance ?: synchronized(this){
            oInstance ?: UsersRepository(usersFbDao)
        }
    }

    // in-memory cache of the loggedInUser object
    var loggedInUser: User? = null
        private set
    val isLoggedIn: Boolean
        get() = loggedInUser != null

    private val _loggedInUsers = mutableListOf<User>()
    val loggedInUsers: List<User> = _loggedInUsers

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
            // додати користувача до списку залогінених
            addLoggedInUser(userData, true)
            return Result.Success(userData)
        }
        catch (e: Exception) {
            Log.e(ContentValues.TAG, "login failure", e)
            return Result.Error(e)
        }
    }

    fun logout(userId: String? = null) : LoginResult {
        val loggingOutUser: User? = if (userId == null)
            loggedInUser
        else
            getLoggedInUser(userId)
        // Вилучити користувача із списку
        _loggedInUsers.remove(loggingOutUser)
        // Замінити поточного користувача
        loggedInUser = if (loggedInUsers.isNotEmpty())
            loggedInUsers[0]
        else null
        // Вийти із Firebase
        // TODO: розібратися з авторизацією Firebase
        FirebaseData.Auth.signOut()
        return LoginResult(ELoginState.LoggedOut, loggingOutUser)
    }

    fun addLoggedInUser(user: User, setAsCurrent: Boolean) : LoginResult {
        // Перевірка на колізію серед користувачів
        if (getLoggedInUser(user.userId) != null) {
            return LoginResult(loginState = ELoginState.None, errorStr = "Такий користувач уже існує")
            // TODO Handle situation
        }
        _loggedInUsers.add(user)
        if (setAsCurrent)
            setLoggedInUser(user)
        return LoginResult(loginState = ELoginState.LoggedIn, user = user)
    }

    suspend fun loginByFirebaseCurrentUser() : LoginResult {
        if (FirebaseData.Auth.currentUser?.uid != null) {
            val currentUserData = getUser(FirebaseData.Auth.currentUser!!.uid)
            if (currentUserData != null) {
                addLoggedInUser(currentUserData, true)
                // Встановити поточного користувача
                loggedInUser = currentUserData
                return LoginResult(ELoginState.LoggedIn, currentUserData)
            }
        }
        return LoginResult(ELoginState.NeedToLogin)
    }

    private fun setLoggedInUser(user: User) {
        // Перевірка на колізію серед користувачів
        if (getLoggedInUser(user.userId) == null) {
            // TODO Handle situation
//            return LoginResult(loginState = ELoginState.None, errorStr = "Такий користувач уже існує")
            Log.e(TAG, "The user isn't logged in")
            return
        }
        this.loggedInUser = user
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    suspend fun getUser(userId: String): User? {
        return usersFbDao.getUser(userId)
    }

    fun getLoggedInUser(userId: String): User? {
        return _loggedInUsers.find { it.userId == userId }
    }

    @Deprecated("Should not be used for now")
    suspend fun getUser(username:String, password:String) : Result<User> {
        return usersFbDao.getUser(username, password)
    }

    suspend fun createUser(userId:String, displayName:String = "", phoneNum: String = ""): User? {
        return usersFbDao.createUser(userId, displayName, phoneNum)
    }

    suspend fun fillUserData(userId: String, displayName: String, nickname: String): User? {
        return usersFbDao.setUserData(userId, displayName, nickname)
    }
}