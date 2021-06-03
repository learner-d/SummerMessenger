package com.summermessenger.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.summermessenger.data.repository.MainRepository
import com.summermessenger.data.repository.UsersRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                usersRepository = MainRepository.usersRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}