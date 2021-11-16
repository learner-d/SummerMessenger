package com.summermessenger.data.db

import android.content.ContentValues.TAG
import android.util.Log
import com.summermessenger.R
import com.summermessenger.SummerMessenger
import com.summermessenger.data.Result
import com.summermessenger.data.model.User
import kotlinx.coroutines.tasks.await

class UsersFbDao(private val db:FireStoreDb) {
    fun getUsers() : List<User>{
        TODO()
    }

    suspend fun createUser(userId: String, displayName: String = "", phoneNumber: String = ""): User? {
        try {
            val user = User(userId, "", displayName, phoneNumber)
            db.users.document(userId).set(user).await()
            return getUser(userId)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getUser(userId:String) : User? {
        val userDoc = db.users.document(userId).get().await()
        if(userDoc.exists()) {
            return User.load(userDoc)
        }
        return null
    }

    @Deprecated("Should not be used")
    suspend fun getUser(username:String, password:String) : Result<User> {
        val userQuery = db.users.whereEqualTo("username", username).get().await()

        if(userQuery.isEmpty)
            return Result.Error(IllegalAccessException(SummerMessenger.instance.getResourceString(R.string.err_username_invalid)))

        val user = User.load(userQuery.first())
        return Result.Success(user!!)
    }

    fun deleteUser(userId: Int){
        TODO()
    }

    //New Code
    fun listenToFirestoreDb() {
        db.users.addSnapshotListener {
            snapshot, e ->
            //Перевірка на наявність помилок
            if (e != null) {
                Log.w(TAG, "Listen failed")
                return@addSnapshotListener
            }
            //Помилки відсутні
            if (snapshot != null){
                //Снапшот отримано
                val documents = snapshot.documents
                documents.forEach{

                }
            }
        }
    }

    // TODO: перевірити на вийняткові ситуації
    suspend fun setUserData(userId: String, displayName: String = "", nickname: String = ""): User? {
        val displayNameTrimmed = displayName.trim()
        val nicknameTrimmed = nickname.trim()
        if (userId.isEmpty())
            return null
        val userDoc = db.users.document(userId).get().await()
        if (!userDoc.exists())
            return null

        val user = User.load(userDoc)!!
        if (displayNameTrimmed.isNotEmpty())
            user.displayName = displayNameTrimmed
        if (nicknameTrimmed.isNotEmpty())
            user.username = nicknameTrimmed
        if (displayNameTrimmed.isNotEmpty() && nicknameTrimmed.isNotEmpty())
            db.users.document(userId).set(user).await()

        return user
    }
}