package com.summermessenger.ui.register

import com.summermessenger.data.model.User

enum class ERegisterState {None, NeedDataFilling, Registered}
data class RegisterResult(val user: User? = null, val error: Any? = null, val registerState:ERegisterState = ERegisterState.None)