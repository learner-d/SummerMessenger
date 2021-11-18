package com.summermessenger.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.summermessenger.data.Result
import com.summermessenger.data.model.User
import com.summermessenger.data.repository.MainRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _registerResult = MutableLiveData<RegisterResult>()
    val registerResult: LiveData<RegisterResult> = _registerResult
    private val _registerFormState = MutableLiveData<RegisterFormState>()
    val registerFormState: LiveData<RegisterFormState> = _registerFormState


    // Створює користувача
    fun createUser(username:String, password:String) {
        // TODO: Перевірити дані
        // Асинхронна робота
        viewModelScope.launch {
            val createUserResult = MainRepository.usersRepository.createUser(username, password)
            val registerResult: RegisterResult
            // Користувача створено
            if (createUserResult is Result.Success){
                registerResult = RegisterResult(user = createUserResult.data,
                    registerState = ERegisterState.NeedDataFilling)
            }
            else if (createUserResult is Result.Error) {
                if (createUserResult.exception is FirebaseAuthUserCollisionException)
                    registerResult = RegisterResult(error = "Користувача з цією електронною адресою вже зареєстровано")
                else
                    registerResult = RegisterResult(error = createUserResult.toString())
            }
            else {
                registerResult = RegisterResult()
            }


            _registerResult.postValue(registerResult)
        }
    }

    fun requestUserDataFilling(user: User) {
        val registerResult = RegisterResult(registerState = ERegisterState.NeedDataFilling,
            user = user)
        _registerResult.value = registerResult
    }

    // Заповнює реєстраційні дані користувача, та завершує реєстрацію
    fun fillUserData(userId: String, displayName:String, nickname:String = "") {
        viewModelScope.launch {
            val registeredUser = MainRepository.usersRepository.fillUserData(userId, displayName, nickname)
            // Перевірка результату
            if (registeredUser != null) {
                _registerResult.postValue(RegisterResult(user = registeredUser, registerState = ERegisterState.Registered))
            } else {
                _registerResult.postValue(RegisterResult(error= "registeredUser = null"))
            }
        }
    }
}