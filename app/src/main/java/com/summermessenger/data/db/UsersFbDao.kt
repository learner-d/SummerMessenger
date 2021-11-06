package com.summermessenger.data.db

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.summermessenger.R
import com.summermessenger.data.Result
import com.summermessenger.data.model.User
import kotlinx.coroutines.tasks.await

class UsersFbDao(private val appCtx: Context, private val db:FireStoreDb) {
    fun getUsers() : List<User>{
        TODO()
    }

    suspend fun createUser(userId: String, displayName: String = "", phoneNumber: String = ""): User? {
        try {
            val user = User("", displayName, phoneNumber)
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

    suspend fun getUser(username:String, password:String) : Result<User> {
        val userQuery = db.users.whereEqualTo("username", username).get().await()

        if(userQuery.isEmpty)
            // TODO: remove access to app context
            return Result.Error(IllegalAccessException(appCtx.getString(R.string.err_username_invalid)))

        val user = User.load(userQuery.first())
        return Result.Success(user)
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
}