package com.summermessenger.data

import com.summermessenger.data.model.User
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    companion object {
        val MockUsers = arrayOf(
            User("real_doer", "Dmytro", "qwerty12"),
            User("nillado", "Albert", "123mc123")
        );
    }

    val users: List<User> = MockUsers.toList();

    fun login(username: String, password: String): Result<User> {
        try {
            val user = users.find { usr -> usr.id == username }
            if (user != null) {
                if(user.password == password)
                    return Result.Success(user)
                else
                    return Result.Error(IllegalAccessException("Неправильний пароль!"))
            }
            return Result.Error(IllegalAccessException("Користувача не знайдено!"))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}