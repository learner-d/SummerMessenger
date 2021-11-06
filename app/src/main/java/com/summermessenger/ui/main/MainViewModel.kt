package com.summermessenger.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summermessenger.data.repository.MainRepository
import com.summermessenger.data.repository.UsersRepository
import com.summermessenger.ui.login.LoginResult
import kotlinx.coroutines.launch

class MainViewModel(private val usersRepository: UsersRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    fun loginDefault(){
        viewModelScope.launch{
            val loginResult = MainRepository.usersRepository.updateCurrentUser()
            _loginResult.postValue(loginResult)
        }
    }
    fun logout(){
        val loginResult = MainRepository.usersRepository.logout()
        _loginResult.postValue(loginResult)
    }
}