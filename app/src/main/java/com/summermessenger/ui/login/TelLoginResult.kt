package com.summermessenger.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class TelLoginResult(
        val success: LoggedInUserView? = null,
        val error: String? = null
)