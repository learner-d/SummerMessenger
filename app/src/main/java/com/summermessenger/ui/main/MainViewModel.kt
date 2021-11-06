package com.summermessenger.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summermessenger.data.repository.MainRepository
import com.summermessenger.data.repository.UsersRepository
import com.summermessenger.ui.login.ELoginState
import com.summermessenger.ui.login.LoginResult
import kotlinx.coroutines.launch

class MainViewModel(private val usersRepository: UsersRepository) : ViewModel() {
    private val _loginState = MutableLiveData<ELoginState>()
    val loginState: LiveData<ELoginState> = _loginState
    fun loginDefault(){
        viewModelScope.launch{
            if(MainRepository.usersRepository.updateCurrentUser())
                _loginState.postValue(ELoginState.LoggedIn)
            else
                _loginState.postValue(ELoginState.LoggedOut)
        }
    }
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    fun logout(){
        MainRepository.usersRepository.logout()
        _loginState.postValue(ELoginState.LoggedOut)
    }
}