package com.summermessenger.data

import com.summermessenger.data.db.FireStoreDb
import com.summermessenger.data.model.User
import com.summermessenger.util.await
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class UsersDataSource(fireStoreDb: FireStoreDb) {
    val usersDbCollection = fireStoreDb.users

    suspend fun login(username: String, password: String): Result<User> {
        try {
            val userQuery = usersDbCollection.whereEqualTo("username", username).get().await()!!

            if(userQuery.isEmpty)
                return Result.Error(IllegalAccessException("Користувача не знайдено!"))

            val user = User.load(userQuery.first())
            if(user.password == password)
                return Result.Success(user)
            else
                return Result.Error(IllegalAccessException("Неправильний пароль!"))

        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}