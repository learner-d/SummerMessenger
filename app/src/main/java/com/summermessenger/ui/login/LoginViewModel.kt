package com.summermessenger.ui.login

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.summermessenger.data.LoginRepository
import com.summermessenger.data.Result

import com.summermessenger.R
import com.summermessenger.data.FirebaseData
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginFormState

    private val  _telLoginFormState = MutableLiveData<TelLoginFormState>()
    val  telLoginFormState: LiveData<TelLoginFormState> = _telLoginFormState

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _telLoginResult = MutableLiveData<LoginResult>()
    val telLoginResult: LiveData<LoginResult> = _telLoginResult

    fun applyPhoneNum(phoneNum: String, activity: Activity){
        // can be launched in a separate asynchronous job
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNum,
                60, TimeUnit.SECONDS,
                activity,
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks()
                {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        FirebaseData.Auth.signInWithCredential(credential).addOnCompleteListener{
                            if(it.isSuccessful){
                                _telLoginResult.postValue(LoginResult(success = LoggedInUserView(displayName = "")))
                            }
                        }
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        _telLoginResult.postValue(LoginResult(error = R.string.verify_failed))
                    }

                    override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(p0, p1)
                    }
                }
        )
    }

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            val result = loginRepository.login(username, password)
            if (result is Result.Success) {
                _loginResult.postValue(LoginResult(success = LoggedInUserView(displayName = result.data.displayName)))
            } else {
                _loginResult.postValue(LoginResult(error = R.string.login_failed))
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginFormState.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginFormState.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginFormState.value = LoginFormState(isDataValid = true)
        }
    }

    fun telLoginDataChanged(phoneNum: String, msgCode:String){
        var phoneNumValid = isPhoneNumValid(phoneNum)
        var msgCodeValid = isMsgCodeValid(msgCode)

        _telLoginFormState.value = TelLoginFormState(isPhoneNumValid = phoneNumValid,
                                                        isMsgCodeValid = msgCodeValid)
    }

    // A placeholder phone number validation check
    private fun isPhoneNumValid(phoneNum: String):Boolean{
        return Patterns.PHONE.matcher(phoneNum).matches()
    }


    // A placeholder message code validation check
    private fun isMsgCodeValid(msgCode:String):Boolean{
        return true
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}