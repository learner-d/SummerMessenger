package com.summermessenger.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

const val FIREBASEDB_USERS = "users"
const val FIREBASEDB_USER_ID = "id"
const val FIREBASEDB_USER_DISPLAYNAME = "displayName"
const val FIREBASEDB_USER_PHONENUMBER = "phoneNumber"
//val FIREBASEDB_REF_ROOT: DatabaseReference = FirebaseDatabase.getInstance().reference

class FirebaseData {
    companion object{
        val Auth: FirebaseAuth = FirebaseAuth.getInstance()
    }
}