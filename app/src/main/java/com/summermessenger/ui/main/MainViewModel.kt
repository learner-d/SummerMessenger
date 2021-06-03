package com.summermessenger.ui.main

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.summermessenger.data.repository.UsersRepository
import com.summermessenger.data.Result

import com.summermessenger.R
import com.summermessenger.data.FirebaseData
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainViewModel(private val usersRepository: UsersRepository) : ViewModel() {
    fun loginDefault(){
        viewModelScope.launch{
            usersRepository.loginDefault()
        }
    }
    fun logout(){
        usersRepository.logout()
    }
}