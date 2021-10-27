package com.summermessenger.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summermessenger.data.Globals
import com.summermessenger.data.repository.UsersRepository

import kotlinx.coroutines.launch

class MainViewModel(private val usersRepository: UsersRepository) : ViewModel() {
    fun loginDefault(){
        viewModelScope.launch{
            Globals.loginManager.updateCurrentUser()
        }
    }
    suspend fun loginDefaultAsync(){
        Globals.loginManager.updateCurrentUser()
    }
    fun logout(){
        Globals.loginManager.logout()
    }
}