package com.summermessenger.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summermessenger.data.Result
import com.summermessenger.data.repository.MainRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult
    private val _registerFormState = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerFormState
    fun registerUser(username:String, password:String) {
        // TODO: Перевірити дані
        // Асинхронна робота
        viewModelScope.launch {
            val registerResult = MainRepository.usersRepository.createUser(username, password)
            if (registerResult is Result.Success){
                _registerResult.postValue(RegisterResult(success = registerResult.data))
            }
            else{
                _registerResult.postValue(RegisterResult(error = registerResult.toString()))
            }
        }
    }
}