package com.summermessenger.ui.login

import com.summermessenger.data.model.User

/**
 * Authentication result : success (user details) or error message.
 */
enum class ELoginState { None, NeedToLogin, TelCodeSent, NeedToRegister, NeedToCompleteRegistration, LoggedIn, LoggedOut }
data class LoginResult(
        val loginState: ELoginState,
        val user: User? = null,
        val errorStrId: Int? = null,
        @Deprecated("prefer to use errorStrId")
        val errorStr: String? = null,
        val telVerificationId: String? = null
)