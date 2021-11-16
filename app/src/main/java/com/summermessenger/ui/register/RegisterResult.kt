package com.summermessenger.ui.register

import com.summermessenger.data.model.User

enum class ERegisterState {None, CreatedAccount, Registered}
data class RegisterResult(val success: User? = null, val error: Any? = null, val registerState:ERegisterState = ERegisterState.None)