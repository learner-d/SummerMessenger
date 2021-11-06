package com.summermessenger.ui.login

import com.summermessenger.data.model.User

/**
 * Authentication result : success (user details) or error message.
 */
enum class ELoginState {None, LoggedIn, LoggedOut}
data class LoginResult(
        val loginState: ELoginState,
        val user: User? = null,
        val error: Int? = null,
)