package com.summermessenger.data.dao

import com.google.firebase.firestore.auth.User

abstract class UsersDao {
    abstract fun getUsers() : List<User>
    abstract fun getUser(userId:Int) : User
    abstract fun deleteUser(userId: Int)
}