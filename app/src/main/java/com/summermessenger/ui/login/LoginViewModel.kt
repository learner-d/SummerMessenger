package com.summermessenger.ui.login

import android.app.Activity
import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.summermessenger.R
import com.summermessenger.data.FirebaseData
import com.summermessenger.data.Result
import com.summermessenger.data.model.User
import com.summermessenger.data.repository.MainRepository
import com.summermessenger.data.repository.UsersRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

enum class ELoginFragment { EmailLoginFragment, TelLoginFragment }

class LoginViewModel(private val usersRepository: UsersRepository) : ViewModel() {
    private val _loginFormState = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginFormState

    private val  _telLoginFormState = MutableLiveData<TelLoginFormState>()
    val  telLoginFormState: LiveData<TelLoginFormState> = _telLoginFormState

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _showingLoginFragment = MutableLiveData<ELoginFragment>()
    val showingLoginFragment: LiveData<ELoginFragment> = _showingLoginFragment

    private var _telVerificationId = ""
    private lateinit var _telToken: PhoneAuthProvider.ForceResendingToken

    // Кол-беки для оброблення процедури верифікації телефону
    private val mPhoneVerificationCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        // Верифікацію завершено
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action
            loginWithFbCredential(credential)
        }
        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)

            @StringRes
            val errorStrId = if (e is FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                R.string.tel_verify_bad_number
            } else if (e is FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                R.string.tel_verify_too_many_requests
            }
            else
                R.string.tel_verify_fail

            // Show a message and update the UI
            _loginResult.postValue(
                LoginResult(
                    ELoginState.None,
                    errorStrId = errorStrId
                )
            )
        }
        override fun onCodeSent(
            verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            _telVerificationId = verificationId
            _telToken = token
            _loginResult.postValue(LoginResult(loginState = ELoginState.TelCodeSent))
        }
    }

    fun requestMsgCode(phoneNum: String, activity: Activity) {
        val phoneNumAuthOptions
            = PhoneAuthOptions.newBuilder(FirebaseData.Auth)
                .setPhoneNumber(phoneNum)
                .setActivity(activity)
                .setTimeout(120, TimeUnit.SECONDS)
                .setCallbacks(mPhoneVerificationCallbacks)
                .build()

        PhoneAuthProvider.verifyPhoneNumber(phoneNumAuthOptions)
    }

    // Перевірка коду з SMS
    fun verifyMsgCode(smsCode:String) {
        val credential = PhoneAuthProvider.getCredential(_telVerificationId, smsCode)
        loginWithFbCredential(credential)
    }

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        viewModelScope.launch {
            val result = MainRepository.usersRepository.login(username, password)
            if (result is Result.Success) {
                _loginResult.postValue(LoginResult(ELoginState.LoggedIn, result.data))
            } else {
                result as Result.Error
                val errorStr = if (result.exception is FirebaseAuthInvalidCredentialsException)
                    R.string.invalid_username_or_password
                else if (result.exception is FirebaseNetworkException)
                    R.string.err_network_error
                else
                    R.string.login_failed

                _loginResult.postValue(LoginResult(ELoginState.None, errorStrId = errorStr))
            }
        }
    }

    fun loginWithFbCredential(credential: AuthCredential) {
        viewModelScope.launch {
            try {
                val fbAuthResult = FirebaseData.Auth.signInWithCredential(credential).await()
                // TODO: Check null reference situation
                val fbUser = fbAuthResult.user!!
                var user = MainRepository.usersRepository.getUser(fbUser.uid)
                val loginState: ELoginState
                if (user != null)
                    loginState = ELoginState.LoggedIn
                else {
                    loginState = ELoginState.NeedToCompleteRegistration
                    user = User(userId = fbUser.uid)
                }

                _loginResult.postValue(LoginResult(loginState = loginState, user = user))
            } catch (e: Exception) {
                // TODO: вирішити момент
                Log.e(TAG, e.toString())
                if (e is FirebaseAuthInvalidCredentialsException){
                    _loginResult.postValue(LoginResult(loginState = ELoginState.None, errorStr = e.localizedMessage ?: e.toString()))
                }
                else {
                    _loginResult.postValue(LoginResult(loginState = ELoginState.None, errorStr = e.toString()))
                }
            }
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginFormState.postValue(LoginFormState(usernameError = R.string.invalid_username))
        } else if (!isPasswordValid(password)) {
            _loginFormState.postValue(LoginFormState(passwordError = R.string.invalid_password))
        } else {
            _loginFormState.postValue(LoginFormState(isDataValid = true))
        }
    }

    fun telLoginDataChanged(phoneNum: String, msgCode:String) {
        val phoneNumValid = isPhoneNumValid(phoneNum)
        val msgCodeValid = isMsgCodeValid(msgCode)

        _telLoginFormState.postValue(TelLoginFormState(isPhoneNumValid = phoneNumValid,
                                                        isMsgCodeValid = msgCodeValid))
    }

    // A placeholder phone number validation check
    private fun isPhoneNumValid(phoneNum: String):Boolean {
        return Patterns.PHONE.matcher(phoneNum).matches()
    }

    // A placeholder message code validation check
    private fun isMsgCodeValid(msgCode:String):Boolean {
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

    // Додає користувача в репозиторій авторизованих
    fun addLoggedInUser(user: User, setAsCurrent: Boolean) {
        val loginResult = MainRepository.usersRepository.addLoggedInUser(user, setAsCurrent)
        _loginResult.value = loginResult
    }

    // Показує заданий фрагмент
    fun showLoginFragment(eLoginFragment: ELoginFragment) {
        _showingLoginFragment.value = eLoginFragment
    }

    // Ініціює виклик активиті "реєстрація"
    fun requestRegistrationActivity() {
        _loginResult.value = LoginResult(loginState = ELoginState.NeedToRegister)
    }
}