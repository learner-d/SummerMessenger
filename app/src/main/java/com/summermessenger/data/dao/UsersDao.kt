package com.summermessenger.data.dao

import android.content.Context
import com.summermessenger.R
import com.summermessenger.SummerMessenger
import com.summermessenger.data.Result
import com.summermessenger.data.db.FireStoreDb
import com.summermessenger.data.model.User
import com.summermessenger.util.await

abstract class UsersDao(val db:FireStoreDb) {
    val context = SummerMessenger.context
    abstract fun getUsers() : List<User>
    private suspend fun getUser(userId:Int) : User? {
        val userDoc = db.users.document("$userId").get().await()
        if(userDoc.exists()){
            val user = User.load(userDoc)
            return user
        }
        return null
    }

    suspend fun getUser(username:String, password:String) : Result<User> {
        val userQuery = db.users.whereEqualTo("username", username).get().await()

        if(userQuery.isEmpty)
            return Result.Error(IllegalAccessException(context.getString(R.string.err_username_invalid)))

        val user = User.load(userQuery.first())
        if(user.password == password)
            return Result.Success(user)
        else
            return Result.Error(IllegalAccessException("Неправильний пароль!"))
    }
    abstract fun deleteUser(userId: Int)
}