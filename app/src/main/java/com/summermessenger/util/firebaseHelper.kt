package com.summermessenger.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

lateinit var FIREBASE_AUTH: FirebaseAuth

lateinit var FIREBASEDB_REF_ROOT: DatabaseReference
const val FIREBASEDB_USERS = "users"
const val FIREBASEDB_USER_ID = "id"
const val FIREBASEDB_USER_DISPLAYNAME = "displayName"
const val FIREBASEDB_USER_PHONENUMBER = "phoneNumber"

fun initFirebase(){
    FIREBASE_AUTH = FirebaseAuth.getInstance()
    FIREBASEDB_REF_ROOT = FirebaseDatabase.getInstance().reference
}