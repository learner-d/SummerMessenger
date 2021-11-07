package com.summermessenger.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summermessenger.data.repository.UsersRepository
import com.summermessenger.ui.login.ELoginState
import com.summermessenger.ui.login.LoginResult
import kotlinx.coroutines.launch

class MainViewModel(private val usersRepository: UsersRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    fun loginDefault() {
        viewModelScope.launch {
            val loginResult = usersRepository.loginByFirebaseCurrentUser()
            _loginResult.postValue(loginResult)
        }
    }
    fun requestNewLogin() {
        _loginResult.postValue(LoginResult(ELoginState.NeedToLogin))
    }
    fun logout() {
        val loginResult = usersRepository.logout()
        _loginResult.postValue(loginResult)
    }
    fun requestCurrentLoginResult() {
        val lastCurrentLoggedInUser = usersRepository.loggedInUser
        val loginResult = if (lastCurrentLoggedInUser == null)
            LoginResult(ELoginState.NeedToLogin)
        else
            LoginResult(ELoginState.LoggedIn, lastCurrentLoggedInUser)
        _loginResult.postValue(loginResult)
    }
}